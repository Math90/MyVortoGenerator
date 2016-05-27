import java.sql.*;
import org.json.*;
import java.lang.*;

public class VortoTest {
	
	VortoTest(){
		
	}

	public void fillData(JSONObject msg) throws ClassNotFoundException{

		// JDBC driver name and database URL
		String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";  
		String DB_URL = "jdbc:derby://localhost:1527/vorto";

		//  Database credentials
		String USER = "vorto";
		String PASS = "vorto";
		
		Class.forName(JDBC_DRIVER);
		Connection conn = null;
		Statement stmt = null;
		
	try{

		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = conn.createStatement();
				
		if(msg.get("topic") == "MyFictiveSensor"){
			
			String FB0= "";
			FB0 += "INSERT INTO battery (precent) VALUES ( ";
			FB0 += msg.getJSONObject("payload").getJSONObject("battery").get("precent") + "";
			FB0 += ")";
			stmt.executeUpdate(FB0);

			String FB1= "";
			FB1 += "INSERT INTO buttons (Button1,Button2) VALUES ( ";
			FB1 += msg.getJSONObject("payload").getJSONObject("buttons").get("Button1") + ",";
			FB1 += msg.getJSONObject("payload").getJSONObject("buttons").get("Button2") + "";
			FB1 += ")";
			stmt.executeUpdate(FB1);

			String EN20= "";
			EN20 += "INSERT INTO Color (r,g,b) VALUES ( ";		
			EN20 += msg.getJSONObject("payload").getJSONObject("ledlights").getJSONObject("Led").get("r") + ",";		
			EN20 += msg.getJSONObject("payload").getJSONObject("ledlights").getJSONObject("Led").get("g") + ",";		
			EN20 += msg.getJSONObject("payload").getJSONObject("ledlights").getJSONObject("Led").get("b") + "";
			EN20 += ")";
			stmt.executeUpdate(EN20);

			String FB2= "";
			FB2 += "INSERT INTO ledlights (Led1Working,Led2Working,Led3Working,Led) VALUES ( ";
			FB2 += msg.getJSONObject("payload").getJSONObject("ledlights").get("Led1Working") + ",";
			FB2 += msg.getJSONObject("payload").getJSONObject("ledlights").get("Led2Working") + ",";
			FB2 += msg.getJSONObject("payload").getJSONObject("ledlights").get("Led3Working") + ",";
			FB2 += "(SELECT MAX(p_key) FROM Color)";
			FB2 += ")";
			stmt.executeUpdate(FB2);

			String EN30= "";
			EN30 += "INSERT INTO Color (r,g,b) VALUES ( ";		
			EN30 += msg.getJSONObject("payload").getJSONObject("messagemonitor").getJSONObject("BackgorundColor").get("r") + ",";		
			EN30 += msg.getJSONObject("payload").getJSONObject("messagemonitor").getJSONObject("BackgorundColor").get("g") + ",";		
			EN30 += msg.getJSONObject("payload").getJSONObject("messagemonitor").getJSONObject("BackgorundColor").get("b") + "";
			EN30 += ")";
			stmt.executeUpdate(EN30);

			String FB3= "";
			FB3 += "INSERT INTO messagemonitor (ActualMessage,BackgorundColor) VALUES ( ";
			FB3 += msg.getJSONObject("payload").getJSONObject("messagemonitor").get("ActualMessage") + ",";
			FB3 += "(SELECT MAX(p_key) FROM Color)";
			FB3 += ")";
			stmt.executeUpdate(FB3);

			String FB4= "";
			FB4 += "INSERT INTO temperaturesensor (ActualTemperature,UnitType) VALUES ( ";
			FB4 += msg.getJSONObject("payload").getJSONObject("temperaturesensor").get("ActualTemperature") + ",";
			FB4 += msg.getJSONObject("payload").getJSONObject("temperaturesensor").get("UnitType") + "";
			FB4 += ")";
			stmt.executeUpdate(FB4);

			String EN50= "";
			EN50 += "INSERT INTO MultiStateSwitch (state) VALUES ( ";		
			EN50 += "(SELECT p_key FROM state WHERE enums ='" + msg.getJSONObject("payload").getJSONObject("switcher").getJSONObject("SwitchState").get("State") + "')";
			EN50 += ")";
			stmt.executeUpdate(EN50);

			String FB5= "";
			FB5 += "INSERT INTO switcher (SwitchState) VALUES ( ";
			FB5 += "(SELECT MAX(p_key) FROM MultiStateSwitch)";
			FB5 += ")";
			stmt.executeUpdate(FB5);

			String EN61= "";
			EN61 += "INSERT INTO Calculation (deltaSpeed,t) VALUES ( ";		
			EN61 += msg.getJSONObject("payload").getJSONObject("accelerometer").getJSONObject("currentAcc").getJSONObject("calc").get("deltaSpeed") + ",";		
			EN61 += msg.getJSONObject("payload").getJSONObject("accelerometer").getJSONObject("currentAcc").getJSONObject("calc").get("t") + "";
			EN61 += ")";
			stmt.executeUpdate(EN61);

			String EN60= "";
			EN60 += "INSERT INTO Acceleration (acceleration,calc) VALUES ( ";		
			EN60 += msg.getJSONObject("payload").getJSONObject("accelerometer").getJSONObject("currentAcc").get("acceleration") + ",";
			EN60 += "(SELECT MAX(p_key) FROM Calculation)";
			EN60 += ")";
			stmt.executeUpdate(EN60);

			String FB6= "";
			FB6 += "INSERT INTO accelerometer (speedUnit,currentAcc) VALUES ( ";
			FB6 += "(SELECT p_key FROM SpeedUnit WHERE enums ='" + msg.getJSONObject("payload").getJSONObject("accelerometer").get("speedUnit") + "'),";
			FB6 += "(SELECT MAX(p_key) FROM Acceleration)";
			FB6 += ")";
			stmt.executeUpdate(FB6);

			String IM = "";
			IM += "INSERT INTO MyFictiveSensor (battery,buttons,ledlights,messagemonitor,temperaturesensor,switcher,accelerometer,measure_time) VALUES ( ";
			IM += "(SELECT MAX(p_key) FROM battery),";
			IM += "(SELECT MAX(p_key) FROM buttons),";
			IM += "(SELECT MAX(p_key) FROM ledlights),";
			IM += "(SELECT MAX(p_key) FROM messagemonitor),";
			IM += "(SELECT MAX(p_key) FROM temperaturesensor),";
			IM += "(SELECT MAX(p_key) FROM switcher),";
			IM += "(SELECT MAX(p_key) FROM accelerometer),";
			IM += msg.getJSONObject("payload").get("measure_time");
			IM += ")";
			stmt.executeUpdate(IM);
		}
	}

	catch(SQLException se){se.printStackTrace();}
	catch(Exception e){e.printStackTrace();}
	finally{
		try{if(stmt!=null) conn.close();}
		catch(SQLException se){}
		try{if(conn!=null) conn.close();}
		catch(SQLException se){se.printStackTrace();}
		}
	}

	public static void main(String [ ] args) throws JSONException, ClassNotFoundException{
		
		JSONObject message = new JSONObject();
		
		//Entities
		JSONObject led = new JSONObject();
		led.put("r", (int )(Math.random() * 255 + 1));
		led.put("g", (int )(Math.random() * 255 + 1));
		led.put("b", (int )(Math.random() * 255 + 1));
		JSONObject backgroundColor = new JSONObject();
		backgroundColor.put("r", (int )(Math.random() * 255 + 1));
		backgroundColor.put("g", (int )(Math.random() * 255 + 1));
		backgroundColor.put("b", (int )(Math.random() * 255 + 1));
		JSONObject multiStateSwitch = new JSONObject();
		multiStateSwitch.put("State", "Up");
		JSONObject speedUnit = new JSONObject();
		JSONObject calculation = new JSONObject();	
		calculation.put("deltaSpeed", (int )(Math.random() * 200 + 1));
		calculation.put("t", (int )(Math.random() * 15 + 1));
		JSONObject acceleration = new JSONObject();	
		acceleration.put("calc", calculation);
		acceleration.put("acceleration", (int )(Math.random() * 200 + 1));
		
		//Functionblocks
		JSONObject battery = new JSONObject();
		battery.put("precent", (int )(Math.random() * 100 + 1));
		JSONObject buttons = new JSONObject();
		buttons.put("Button1", (Math.random() < 0.5));
		buttons.put("Button2", (Math.random() < 0.5));
		JSONObject ledLights = new JSONObject();
		ledLights.put("Led1Working", (Math.random() < 0.5));
		ledLights.put("Led2Working", (Math.random() < 0.5));
		ledLights.put("Led3Working", (Math.random() < 0.5));
		ledLights.put("Led", led);
		JSONObject messageMonitor = new JSONObject();
		messageMonitor.put("ActualMessage", "'HelloWorld'");
		messageMonitor.put("BackgorundColor", backgroundColor);
		JSONObject temperatureSensor = new JSONObject();
		temperatureSensor.put("ActualTemperature", (int )(Math.random() * 40 + 1));
		temperatureSensor.put("UnitType", 0);
		JSONObject switcher = new JSONObject();
		switcher.put("SwitchState", multiStateSwitch);
		JSONObject acceleroMeter = new JSONObject();
		acceleroMeter.put("currentAcc", acceleration);
		acceleroMeter.put("speedUnit", "kmph");

		//Information Model
		JSONObject payload = new JSONObject();
		payload.put("measure_time", "'2016-05-11 8:10:10'");
		payload.put("battery", battery);
		payload.put("buttons", buttons);
		payload.put("ledlights", ledLights);
		payload.put("messagemonitor", messageMonitor);
		payload.put("temperaturesensor", temperatureSensor);
		payload.put("switcher", switcher);
		payload.put("accelerometer", acceleroMeter);
		
		message.put("topic", "MyFictiveSensor");
		message.put("payload", payload);
		
		VortoTest MyTest = new VortoTest();
		
		MyTest.fillData(message);

		
	}
}























