#ifndef FX2_H
#pragma once  		// for windows c++
#endif

//
//	Stream Shared File
//	These Interface Definitions are Shared btw the Cypress 8051 Controller and Host Driver
//

// Product ID's Used to Select Platform
#define _V31x_PLATFORM_PID_		0x0021
#define _M31x_PLATFORM_PID_		0x0041

/*
**	Start of Frame Tag
*/
#define LDI_WRITE_CMD			0x70
#define LDI_READ_CMD			0x71
#define MAX_PAYLOAD_SIZE 		54				// 54 is phy. max
#define EEPROM_MAX_BYTES		8192			// Number of Bytes in EEPROM

/*
**	LED States as Reported in SOF Header
*/
typedef enum
{
	LED_OFF,
	LED_PD,
	LED_STATE_ONE,
	LED_STATE_TWO,
	LED_STATE_THREE,
	LED_TIR
} LED_State_Type;

/*
**	Control Transfer Packet Definition
**	MUST be 64 Bytes in Length
*/
typedef struct
{
	BYTE id;
	BYTE cmd;
	BYTE Device_Address;
	BYTE Register_Address;
	BYTE Address_MSB;
	BYTE Address_LSB;
	BYTE NumBytes;
	BYTE Value1;
	BYTE Value2;
	BYTE Width;
	BYTE Data[MAX_PAYLOAD_SIZE];
} _CONTROL_PACKET_TYPE;

/*
**	Vendor Specific Interface Commands
*/
typedef enum
{
	LOOPBACK_WRITE	= 0x20,
	LOOPBACK_READ,
	JTAG_INIT,
	JTAG_EXEC,
	JTAG_WRITE,
	JTAG_READ,
	JTAG_STOP,
	PORTA_WRITE,
	PORTA_READ,
	PORTD_WRITE,
	PORTD_READ,
	I2C_WRITE,
	I2C_READ,
	I2C_EEPROM_WRITE,
	I2C_EEPROM_READ,
	TWI_WRITE,
	TWI_READ,
	TWI_EEPROM_WRITE,
	TWI_EEPROM_READ,
	CONFIG_PORT_DIR,
	XI_SER_WRITE,
	XI_SER_ENABLE,
	GET_FW_REVISION,
	GET_CPLD_REVISION,
	LDI_RENUM
} Vendor_Interface_Type;

/*
**	EEPROM Format
*/
typedef struct
{
	/*
	**	1st 8 Bytes MUST Comply with Cypress Boot Loader Format
	**	DO NOT MODIFY
	*/
	BYTE 
		type,				// Boot Must be either 0xC0 or 0xC2
		VID_LSB,
		VID_MSB,
		PID_LSB,			// Pre-Enum Product ID
		PID_MSB,
		DEVID_LSB,
		DEVID_MSB,
		CONFIG;
	// end required by Cypress format

	// start Lumidigm assignement
	DWORD
		Serial_Number,
		CPLD_Firmware_Revision,
		ManDateCode;

	WORD
		Product_ID,			// Post-Enum Product ID
		Platform_Type,
		Bx_Row,				// V31x Only
		Bx_Col,				// V31x Only
		PD_Row,				// M31x Only
		PD_Col,				// M31x Only
		DPI,				// V31x Only
		MfgStateFlag;		// Manufacturing state flag

	char pCalData[1520];	// V31x Only
	char pTagData[256];		// For V100_Set_Tag and V100_Get_Tag functionality first byte is size
	
} EEPROM_Format_Type;

