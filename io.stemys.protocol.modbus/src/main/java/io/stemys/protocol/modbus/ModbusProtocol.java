/*******************************************************************************
 * Copyright (c) 2014, 2018 stemys SA
 *******************************************************************************/
package io.stemys.protocol.modbus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.util.BitVector;

import io.stemys.platform.core.thing.model.protocol.DataType;
import io.stemys.platform.core.thing.model.protocol.ProtocolModel;
import io.stemys.platform.core.thing.query.OperatorFilter;

/**
 * 
 * @author Luna
 *
 */
public class ModbusProtocol implements ProtocolModel {
	/**
	 * 
	 */
	private static final Logger s_logger = LoggerFactory.getLogger(ModbusProtocol.class);
	/**
	 * 
	 */
	private static ConcurrentHashMap<String, ModbusTCPMaster> masterByIp = new ConcurrentHashMap<String, ModbusTCPMaster>();
	/**
	 * 
	 */
	private static ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>> addressCounterByIp = new ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>>();

	/**
	 * 
	 * @author Luna
	 *
	 */
	public enum ReadMethod {
		READ_INPUT_REGISTER, READ_HOLDING_REGISTER, READ_INPUTS_STATUS
	}

	/**
	 * 
	 * @author Luna
	 *
	 */
	public enum ProtocolConfig {
		IP, PORT, ADDRESS, LENGTH, READ_METHOD, UNITID
	}

	/**
	 * 
	 * @author Luna
	 *
	 */
	public enum Parameter {
		READ_METHOD, ADDRESS, LENGTH, OFFSET, REVERSE_WORD, FACTOR, READ_EVERY
	}

	// ----------------------------------------------------------------
	//
	// Dependencies
	//
	// ----------------------------------------------------------------
	/**
	 * 
	 */
	public ModbusProtocol() {
		super();

	}

	// ----------------------------------------------------------------
	//
	// Activation APIs
	//
	// ----------------------------------------------------------------
	/**
	 * 
	 * @param componentContext
	 * @param properties
	 */
	protected void activate(ComponentContext componentContext, Map<String, Object> properties) {
		s_logger.info("Activating Modbus Protocol...");

		s_logger.info("Activating Modbus Protocol... Done.");
	}

	/**
	 * 
	 * @param componentContext
	 */
	protected void deactivate(ComponentContext componentContext) {
		s_logger.debug("Deactivating Modbus Protocol....");

		if (masterByIp != null && !masterByIp.isEmpty())
			for (Entry<String, ModbusTCPMaster> e : masterByIp.entrySet())
				disconnnect(e.getKey());

		s_logger.debug("Deactivating Modbus Protocol... Done.");
	}

	@Override
	public boolean write(Map<String, String> configParameters, Map<String, String> paramters, DataType dataType, Object... value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object read(Map<String, String> configParameters, Map<String, String> itemParameters, DataType type) {

		Object result = null;
		String ip = null;

		try {

			if (configParameters == null) {
				s_logger.error("Config parameters is null !");
				return null;
			}

			if (itemParameters == null) {
				s_logger.error("Parameters is null !");
				return null;
			}

			if (!configParameters.containsKey(ProtocolConfig.IP.name())) {
				s_logger.error("Modbus IP is not set...");
				return null;
			}

			if (!configParameters.containsKey(ProtocolConfig.PORT.name())) {
				s_logger.error("Modbus port is not set...");
				return null;
			}

			ip = String.valueOf(configParameters.get(ProtocolConfig.IP.name()));
			short port = Short.parseShort(configParameters.get(ProtocolConfig.PORT.name()));

			ReadMethod protocolReadMethod = null;
			Integer protocolAddress = null;
			Integer protocolUnitId = 1;

			if (configParameters.containsKey(ProtocolConfig.ADDRESS.name()))
				protocolAddress = Integer.valueOf(configParameters.get(ProtocolConfig.ADDRESS.name()));

			boolean isOnline = connect(ip, port);

			if (!isOnline)
				return result;

			/**
			 * <parameter id="OFFSET">12</parameter>
			 * <parameter id="REVERSE_WORD">true</parameter>
			 * <parameter id="FACTOR">1</parameter> <parameter id="LENGTH">2</parameter>
			 * <parameter id="READ_EVERY">10</parameter>
			 */

			Double factor = 1.0;
			Boolean reverseWord = false;
			Integer length = 1, offset = 1;

			if (itemParameters.containsKey(ProtocolConfig.READ_METHOD.name()))
				protocolReadMethod = ReadMethod.valueOf(itemParameters.get(ProtocolConfig.READ_METHOD.name()));

			if (itemParameters.containsKey(ProtocolConfig.UNITID.name()))
				protocolUnitId = Integer.valueOf(itemParameters.get(ProtocolConfig.UNITID.name()));

			if (itemParameters.containsKey(Parameter.OFFSET.name()))
				offset = Integer.parseInt(itemParameters.get(Parameter.OFFSET.name()));
			if (itemParameters.containsKey(Parameter.LENGTH.name()))
				length = Integer.parseInt(itemParameters.get(Parameter.LENGTH.name()));

			if (itemParameters.containsKey(Parameter.REVERSE_WORD.name()))
				reverseWord = Boolean.valueOf(itemParameters.get(Parameter.REVERSE_WORD.name()));

			if (itemParameters.containsKey(Parameter.FACTOR.name()))
				factor = Double.valueOf(itemParameters.get(Parameter.FACTOR.name()));

			InputRegister[] registers = readRegister(ip, port, protocolReadMethod, protocolUnitId, protocolAddress + offset,
					length);

			result = processInputRegister(registers, 0, length, reverseWord, factor, type);

			return result;

		} catch (Exception ex) {
			s_logger.error("Error on reading protocol modbus for an item ", ex);
			return result;
		}
	}

	/**
	 * 
	 * @param results
	 * @param offset
	 * @param length
	 * @param reverseWord
	 * @param factor
	 * @param type
	 * @return
	 */
	private Object processInputRegister(InputRegister[] results, Integer offset, Integer length, Boolean reverseWord, Double factor,
			DataType type) {

		if (type == null) {

			s_logger.error("Unable to readRegister, missing TYPE parameter ...");
			return null;
		}

		if (results == null || results.length == 0) {
			s_logger.error("Unable to readRegister, missing TYPE parameter ...");
			return null;
		}

		switch (type) {

		case BOOLEAN:
			return results[offset].getValue() == 1;

		case INTEGER:
			if (length == 1)
				return results[offset].getValue();
			else if (length == 2) { // 32 bit
				int i1 = results[offset].getValue();
				int i2 = results[offset + 1].getValue();

				// Inversion de mot
				if (reverseWord)
					return (i2 | (i1 << 16));
				else
					return (i1 | (i2 << 16));

			}
			return 0;

		case LONG:
			if (length == 1)
				return (long) results[offset].getValue();
			else if (length == 2) {// 32 bit

				int l1 = results[offset].getValue();
				int l2 = results[offset + 1].getValue();

				// Inversion de mot
				if (reverseWord)
					return (long) (l2 | (l1 << 16));
				else
					return (long) (l1 | (l2 << 16));
			}
			return 0L;

		case DOUBLE:
			if (length >= 2) {
				int d1 = results[offset].getValue();
				int d2 = results[offset + 1].getValue();
				int res = 0;
				// Inversion de mot
				if (reverseWord)
					res = d2 | (d1 << 16);
				else
					res = d1 | (d2 << 16);
				float f = Float.intBitsToFloat(res);
				return new Float(f).doubleValue() * factor;
			}

		case STRING:
			StringBuffer sb = new StringBuffer();
			byte[] data = new byte[2];

			for (int idx = 0; idx < length; idx++) {

				data[0] = (byte) (results[offset + idx].getValue() & 0xFF);
				data[1] = (byte) ((results[offset + idx].getValue() >> 8) & 0xFF);

				if (idx == 0 && data[0] == 0)
					return "";

				sb.append((char) data[0]);
				sb.append((char) data[1]);
			}
			int idx = sb.toString().indexOf(0);
			String result = sb.toString();
			if (idx >= 0)
				result = result.substring(0, result.indexOf(0));

			return result;

		default:
			return null;
		}

	}

	/**
	 * 
	 * @param ip
	 * @param port
	 * @param method
	 * @param unitId
	 * @param address
	 * @param length
	 * @return
	 */
	private InputRegister[] readRegister(String ip, short port, ReadMethod method, Integer unitId, Integer address,
			Integer length) {

		if (ip == null) {
			s_logger.error("Unable to readRegister, missing IP parameter ...");
			return null;
		}

		if (method == null) {
			s_logger.error("Unable to readRegister, missing READ_METHOD parameter ...");
			return null;
		}

		if (unitId == null) {
			s_logger.error("Unable to readRegister, missing UNIT_ID parameter ...");
			return null;
		}
		if (address == null) {
			s_logger.error("Unable to readRegister, missing ADDRESS parameter ...");
			return null;
		}

		if (length == null) {
			s_logger.error("Unable to readRegister, missing LENGTH parameter ...");
			return null;
		}

		s_logger.debug("We'll try to read {} with a length of {}", address, length);
		ModbusTCPMaster master = masterByIp.get(ip);
		try {
			InputRegister[] results = null;
			switch (method) {

			case READ_HOLDING_REGISTER:
				results = master.readMultipleRegisters(unitId, address, length);
				break;
			case READ_INPUT_REGISTER:
				results = master.readInputRegisters(unitId, address, length);
				break;
			case READ_INPUTS_STATUS:
				BitVector vector = master.readInputDiscretes(unitId, address, length);
				// TODO : do something with this vector
			}

			return results;

		} catch (ModbusException e) {
			s_logger.error("unable to read modbus on ip {}, for unitid {}, address {}, length {}", ip, unitId, address, length, e);
			return null;
		}
	}

	/**
	 * 
	 * @param ip
	 * @param port
	 * @return
	 */
	private boolean connect(String ip, short port) {
		if (!masterByIp.containsKey(ip))
			masterByIp.put(ip, new ModbusTCPMaster(ip, port));

		ModbusTCPMaster master = masterByIp.get(ip);

		try {

			master.connect();
			return true;
		} catch (Exception e) {
			s_logger.error("Cannot connect to slave", e);
			disconnnect(ip);
			return false;
		}
	}

	/**
	 * 
	 * @param ip
	 */
	private void disconnnect(String ip) {

		ModbusTCPMaster master = masterByIp.get(ip);
		if (master != null) {
			master.disconnect();
			masterByIp.remove(ip);
		}
	}

	@Override
	public Map<String, Object> read(Map<String, String> configParameters, Map<String, Map<String, String>> parameters,
			Map<String, DataType> dataTypes) {

		final HashMap<String, Object> results = new HashMap<String, Object>();

		String ip = null;

		try {
			if (configParameters == null) {
				s_logger.error("Config parameters is null !");
				return null;
			}

			if (parameters == null) {
				s_logger.error("Parameters is null !");
				return null;
			}

			if (!configParameters.containsKey(ProtocolConfig.IP.name())) {
				s_logger.error("Modbus IP is not set...");
				return null;
			}

			if (!configParameters.containsKey(ProtocolConfig.PORT.name())) {
				s_logger.error("Modbus port is not set...");
				return null;
			}

			ip = String.valueOf(configParameters.get(ProtocolConfig.IP.name()));
			short port = Short.parseShort(configParameters.get(ProtocolConfig.PORT.name()));

			ReadMethod protocolReadMethod = null;
			Integer protocolAddress = null;
			Integer protocolLength = 1;
			Integer protocolUnitId = 1;

			if (configParameters.containsKey(ProtocolConfig.READ_METHOD.name()))
				protocolReadMethod = ReadMethod.valueOf(configParameters.get(ProtocolConfig.READ_METHOD.name()));

			if (configParameters.containsKey(ProtocolConfig.ADDRESS.name()))
				protocolAddress = Integer.valueOf(configParameters.get(ProtocolConfig.ADDRESS.name()));

			if (configParameters.containsKey(ProtocolConfig.LENGTH.name()))
				protocolLength = Integer.valueOf(configParameters.get(ProtocolConfig.LENGTH.name()));

			if (configParameters.containsKey(ProtocolConfig.UNITID.name()))
				protocolUnitId = Integer.valueOf(configParameters.get(ProtocolConfig.UNITID.name()));

			s_logger.debug("Try to read on ip {} and port {}", ip, port);
			s_logger.debug("We get protocol address {}, protocol length {} and protocol read method {}", protocolAddress,
					protocolLength, protocolReadMethod);

			boolean isOnline = connect(ip, port);

			if (!isOnline)
				return results;

			InputRegister[] registers = new InputRegister[(int) (Math.ceil((double) protocolLength / 100)) * 100];
			// s_logger.debug("We generate a {} length registers", registers.length);
			for (int i = 0; i < protocolLength; i += 100) {
				InputRegister[] tempRegisters = readRegister(ip, port, protocolReadMethod, protocolUnitId, protocolAddress + i,
						100);
				// s_logger.debug("We copy {} byte at index {}", tempRegisters.length, i);
				System.arraycopy(tempRegisters, 0, registers, i, 100);
			}

			for (Entry<String, Map<String, String>> e : parameters.entrySet()) {
				DataType type = dataTypes.get(e.getKey());
				Map<String, String> itemParameters = e.getValue();

				/**
				 * <parameter id="OFFSET">12</parameter>
				 * <parameter id="REVERSE_WORD">true</parameter>
				 * <parameter id="FACTOR">1</parameter> <parameter id="LENGTH">2</parameter>
				 * <parameter id="READ_EVERY">10</parameter>
				 */

				Double factor = 1.0;
				Boolean reverseWord = false;
				Integer length = 1, offset = 1;
				Integer readEvery = 0;

				if (itemParameters.containsKey(Parameter.OFFSET.name()))
					offset = Integer.parseInt(itemParameters.get(Parameter.OFFSET.name()));
				if (itemParameters.containsKey(Parameter.LENGTH.name()))
					length = Integer.parseInt(itemParameters.get(Parameter.LENGTH.name()));

				if (itemParameters.containsKey(Parameter.REVERSE_WORD.name()))
					reverseWord = Boolean.valueOf(itemParameters.get(Parameter.REVERSE_WORD.name()));

				if (itemParameters.containsKey(Parameter.FACTOR.name()))
					factor = Double.valueOf(itemParameters.get(Parameter.FACTOR.name()));

				if (itemParameters.containsKey(Parameter.READ_EVERY.name()))
					readEvery = Integer.parseInt(itemParameters.get(Parameter.READ_EVERY.name()));

				Object finalResult = processInputRegister(registers, offset, length, reverseWord, factor, type);
				if (readEvery > 0) {
					int idx = protocolAddress + offset;
					s_logger.debug("We need to read every {} times offset {}", readEvery, offset);
					if (!addressCounterByIp.containsKey(ip)) {
						ConcurrentHashMap<Integer, Integer> a = new ConcurrentHashMap<Integer, Integer>();
						addressCounterByIp.put(ip, a);
					}

					if (!addressCounterByIp.get(ip).containsKey(idx)) {
						addressCounterByIp.get(ip).put(idx, readEvery);
					}

					Integer re = addressCounterByIp.get(ip).get(idx);

					if (re > 0) {
						s_logger.debug("		Remaining {} before reading again", re);
						re -= 1;
						finalResult = null;
						addressCounterByIp.get(ip).put(idx, re);
					} else {
						s_logger.debug("		We read and reset counter");
						addressCounterByIp.get(ip).put(idx, readEvery);
					}
				}
				results.put(e.getKey(), finalResult);
			}
			return results;
		} catch (Exception ex) {
			s_logger.error("Error on reading protocol modbus for a thing ", ex);
			return results;
		}
	}

	@Override
	public List<Map<String, String>> autodiscover(Map<String, String> configParameters, Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> autodiscover(Map<String, String> arg0, Map<String, Map<String, String>> arg1,
			Map<String, DataType> arg2, OperatorFilter arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object execute(Map<String, String> arg0, Map<String, String> arg1, String arg2, DataType arg3,
			Map<String, Object> arg4) {
		// TODO Auto-generated method stub
		return null;
	}

}
