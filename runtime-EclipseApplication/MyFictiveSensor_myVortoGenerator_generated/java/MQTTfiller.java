
	public void fillData(JSONObject msg){

		// JDBC driver name and database URL
		String JDBC_DRIVER = "$JDBCDriver";  
		String DB_URL = "$JDBCURL";

		//  Database credentials
		String USER = "$username";
		String PASS = "$password";

		Connection conn = null;
		Statement stmt = null;
		
	try{

		Class.forName(JDBC_DRIVER);
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
			FB4 += "(SELECT p_key FROM TemperatureType WHERE enums =" + msg.getJSONObject("payload").getJSONObject("temperaturesensor").get("TemperatureType") + ")";
			FB4 += ")";
			stmt.executeUpdate(FB4);

			String EN50= "";
			EN50 += "INSERT INTO MultiStateSwitch (state) VALUES ( ";		
			EN50 += "(SELECT p_key FROM State WHERE enums =" + msg.getJSONObject("payload")getJSONObject("switcher").getJSONObject("SwitchState").get("State") + " )";
			EN50 += ")";
			stmt.executeUpdate(EN50);

			String FB5= "";
			FB5 += "INSERT INTO switcher (SwitchState) VALUES ( ";
			FB5 += "(SELECT MAX(p_key) FROM MultiStateSwitch)";
			FB5 += ")";
			stmt.executeUpdate(FB5);

			String EN61= "";
			EN61 += "INSERT INTO Calculation (deltaSpeed,time) VALUES ( ";		
			EN61 += msg.getJSONObject("payload").getJSONObject("accelerometer").getJSONObject("currentAcc").getJSONObject("calc").get("deltaSpeed") + ",";		
			EN61 += msg.getJSONObject("payload").getJSONObject("accelerometer").getJSONObject("currentAcc").getJSONObject("calc").get("time") + "";
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
			FB6 += "(SELECT p_key FROM SpeedUnit WHERE enums =" + msg.getJSONObject("payload").getJSONObject("accelerometer").get("SpeedUnit") + "),";
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