package org.eclipse.vorto.example

import java.util.ArrayList
import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.common.util.EList
import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask
import org.eclipse.vorto.codegen.api.GenerationResultZip
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.IMappingContext
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.datatype.PropertyType
import org.eclipse.vorto.core.api.model.datatype.Type
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class MyFirstGenerator implements IVortoCodeGenerator {

	override generate(InformationModel infomodel, IMappingContext mappingContext) {
		var output = new GenerationResultZip(infomodel, getServiceKey());
		var generator = new ChainedCodeGeneratorTask<InformationModel>();
		generator.addTask(new GeneratorTaskFromFileTemplate(new SQLTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate(new JavaTemplate()));
		generator.generate(infomodel, mappingContext, output);
		return output
	}

	public static class SQLTemplate implements IFileTemplate<InformationModel> {
		
		var tableList = new ArrayList<String>
		
		override getFileName(InformationModel context) {
			return "creator.sql"
		}

		override getPath(InformationModel context) {
			return "sql"
		}

		override getContent(InformationModel context) {
			var result = '';
			var maindata = '';
			tableList.add(context.name);
			maindata += 'CREATE TABLE ' + context.name + ';' + System.getProperty("line.separator");
			maindata += 'ALTER TABLE ' + context.name + System.getProperty("line.separator");
			for (FunctionblockProperty fbp : context.getProperties()) {
				tableList.add(fbp.getType().name);

				maindata += 'ADD ' + fbp.name + ' INTEGER NOT NULL ' + System.getProperty("line.separator");				
				maindata += 'ADD FOREIGN KEY(' + fbp.name + ')' + System.getProperty("line.separator");
				maindata += 'REFERENCES ' + fbp.getType().name + '(p_key)' + System.getProperty("line.separator");

				var fb = fbp.getType().getFunctionblock();
				for (Type type : getReferencedEnumsAndEntities(fb)) {
					if(type instanceof Entity){
					result += generateForEntity(type);
					}
					if(type instanceof Enum){
					result += generateForEnum(type);
					}
				}

				result += "CREATE TABLE " + fbp.getType().name + ";" + System.getProperty("line.separator");
				result += "ALTER TABLE " + fbp.name + System.getProperty("line.separator");

				if (fbp.getType().getFunctionblock().status != null) {
					result += generateFunctionblockStatus(fbp.getType().getFunctionblock());
				}
				if (fbp.getType().getFunctionblock().fault != null) {
					result += generateFunctionblockFault(fbp.getType().getFunctionblock());
				}
				if (fbp.getType().getFunctionblock().configuration != null) {
					result += generateFunctionblockConfiguration(fbp.getType().getFunctionblock());
				}
				if (fbp.getType().getFunctionblock().events != null) {
					result += generateFunctionblockEvents(fbp.getType().getFunctionblock());
				}
				result += "ADD p_key INTEGER NOT NULL AUTO_INCREMENT" + System.getProperty("line.separator");
				result += "ADD PRIMARY KEY (p_key)" + System.getProperty("line.separator");
				result += ";" + System.getProperty("line.separator") + System.getProperty("line.separator");
				
			}
			maindata += 'ADD measure_time TIME;'
			result += maindata;
			return result;
		}

		def generateForEnum(Enum enum1) {
			var result = ''
			var index = 0;
			result += "CREATE TABLE " + enum1.name + System.getProperty("line.separator");
			result += "(" + System.getProperty("line.separator") 
					+ "enums VARCHAR(20)," + System.getProperty("line.separator") 
					+ "p_key INTEGER NOT NULL," + System.getProperty("line.separator") 
					+ "PRIMARY KEY (p_key)" + System.getProperty("line.separator") 
					+ ");" +System.getProperty("line.separator");
			result += "INSERT INTO " + enum1.name + " (enums,p_key)" + System.getProperty("line.separator") 
					+ 'VALUES (' + System.getProperty("line.separator");
			for (content : enum1.enums) {
				result += "(" + content.name + " , " + index + ")," + System.getProperty("line.separator");
				index++;
			}
			result += ");" + System.getProperty("line.separator") + System.getProperty("line.separator");
			return result
		}

		def getReferencedEnumsAndEntities(FunctionBlock fb) {
			var list = new BasicEList<Type>();
			for (Type type : getReferencedTypesFB(fb)) {
				if (!list.contains(type)) {
					list.add(type as Type);			
				}
			}
			return list;
		}

		def generateForEntity(Entity entity) {
			var result = ''
			result += "CREATE TABLE " + entity.name + ";" + System.getProperty("line.separator");
			result += "ALTER TABLE " + entity.name + System.getProperty("line.separator");
			for (content : entity.properties) {
				result += "ADD " + content.name + " ";
				if (content.type instanceof PrimitivePropertyType) {
					result +=
						mapPrimitiveType(content.type as PrimitivePropertyType) + System.getProperty("line.separator");
				}
				if (content.type instanceof ObjectPropertyType) {
					result += "INTEGER NOT NULL" + System.getProperty("line.separator");
					result += "ADD FOREIGN KEY (" + content.name + ")" + System.getProperty("line.separator")
					result += "REFERENCES "
					result += mapOjectType(content.type) + "(p_key)" + System.getProperty("line.separator");		
				}
			}
			result += "ADD p_key INTEGER NOT NULL AUTO_INCREMENT" + System.getProperty("line.separator");
			result += "ADD PRIMARY KEY (p_key)" + System.getProperty("line.separator");
			result += ";" + System.getProperty("line.separator") + System.getProperty("line.separator");
			return result
		}

		def getReferencedTypesFB(FunctionBlock fb) {
			var types = new BasicEList<Type>();
			if (fb != null) {
				if (fb.getStatus() != null) {
					for (Property property : fb.getStatus().getProperties()) {
						types.addAll(getReferencedTypesP(property));
					}
				}
				if (fb.getConfiguration() != null) {
					for (Property property : fb.getConfiguration().getProperties()) {
						types.addAll(getReferencedTypesP(property));
					}
				}
				if (fb.getFault() != null) {
					for (Property property : fb.getFault().getProperties()) {
						types.addAll(getReferencedTypesP(property));
					}
				}
			}
			return types;
		}

		def getReferencedTypesP(Property property) {
			var types = new BasicEList<Type>();
			if (property.getType() instanceof ObjectPropertyType) {
				var objectType = property.getType() as ObjectPropertyType
				if(!tableList.contains(objectType.getType().name)){
					tableList.add(objectType.getType().name);
					types.add(objectType.getType());
					if (objectType.getType() instanceof Entity) {
						types.addAll(getReferencedTypesT(objectType.getType() as Entity));
						}
					}
			}
			return types
		}

		def getReferencedTypesT(Type type) {
			var EList<Type> types = new BasicEList<Type>()
			if (type instanceof Entity) {
				var Entity entityType = (type as Entity)
				for (Property property : entityType.getProperties()) {
					types.addAll(getReferencedTypesP(property) as BasicEList<Type>)
				}
				types.add(entityType.getSuperType())
			}
			return types
		}

		def generateFunctionblockEvents(FunctionBlock block) {
			var result = '';
			for (content : block.events) {
				if (content instanceof PrimitivePropertyType) {
					result += "ADD " + content.name + " " + content.type.getName() + System.getProperty("line.separator")
				}
				if (content instanceof ObjectPropertyType) {
					result += "ADD " + content.name + " INTEGER NOT NULL" + System.getProperty("line.separator")
					result += "ADD FOREIGN KEY (" + content.name + ")" + System.getProperty("line.separator")
					result += "REFERENCES "
					result += mapOjectType(content) + "(p_key)" + System.getProperty("line.separator");				}
			}
			return result;
		}

		def generateFunctionblockConfiguration(FunctionBlock block) {
			var result = '';
			for (content : block.configuration.properties) {
				result += "ADD " + content.name + " ";
				if (content.type instanceof PrimitivePropertyType) {
					result += mapPrimitiveType(content.type as PrimitivePropertyType) + System.getProperty("line.separator");
				}
				if (content.type instanceof ObjectPropertyType) {
					result += "INTEGER NOT NULL" + System.getProperty("line.separator")
					result += "ADD FOREIGN KEY (" + content.name + ")" + System.getProperty("line.separator")
					result += "REFERENCES "
					result += mapOjectType(content.type) + "(p_key)" + System.getProperty("line.separator");				}
			}
			return result;
		}

		def generateFunctionblockFault(FunctionBlock block) {
			var result = '';
			for (content : block.fault.properties) {
				result += "ADD " + content.name + " ";
				if (content.type instanceof PrimitivePropertyType) {
					result +=
						mapPrimitiveType(content.type as PrimitivePropertyType) + System.getProperty("line.separator");
				}
				if (content.type instanceof ObjectPropertyType) {
					result += "INTEGER NOT NULL" + System.getProperty("line.separator")
					result += "ADD FOREIGN KEY (" + content.name + ")" + System.getProperty("line.separator")
					result += "REFERENCES "
					result += mapOjectType(content.type) + "(p_key)" + System.getProperty("line.separator");				}
			}
			return result;
		}

		def generateFunctionblockStatus(FunctionBlock block) {
			var result = '';
			for (content : block.status.properties) {
				result += "ADD " + content.name + " ";
				if (content.type instanceof PrimitivePropertyType) {
					result +=
						mapPrimitiveType(content.type as PrimitivePropertyType) + System.getProperty("line.separator");
				}
				if (content.type instanceof ObjectPropertyType) {
					result += "INTEGER NOT NULL" + System.getProperty("line.separator")
					result += "ADD FOREIGN KEY (" + content.name + ")" + System.getProperty("line.separator")
					result += "REFERENCES "
					result += mapOjectType(content.type) + "(p_key)" + System.getProperty("line.separator");
				}
			}
			return result;
		}

		def mapPrimitiveType(PrimitivePropertyType type) {
			var result = '';
			if (type.type.getName() == "boolean") {
				result += 'BOOLEAN'
			} else if (type.type.getName() == "int") {
				result += 'INTEGER'
			} else if (type.type.getName() == "double") {
				result += 'DOUBLE PRECISION'
			} else if (type.type.getName() == "datetime") {
				result += 'TIMESTAMP'
			} else if (type.type.getName() == "float") {
				result += 'FLOAT'
			} else if (type.type.getName() == "string") {
				result += 'CHARACTER(20)'
			} else if (type.type.getName() == "long") {
				result += 'BIGINT'
			} else if (type.type.getName() == "short") {
				result += 'SMALLINT'
			}
			return result;
		}

		def mapOjectType(PropertyType type) {
			var result = '';
			var ObjectPropertyType object = type as ObjectPropertyType;
			if (object.type instanceof Entity) {
				result += (object.type as Entity).name
			} else if (object.type instanceof Enum) {
				result += (object.type as Enum).name
			}
			return result;
		}

	}

	public static class JavaTemplate implements IFileTemplate<InformationModel> {
		
		override getFileName(InformationModel context) {
			return "MQTTfiller.java"
		}
		
		override getPath(InformationModel context) {
			return "java"
		}
		
		override getContent(InformationModel context) {
			var result =''
			var index=0
			var fbpNames = new ArrayList<String>
			
			result+= '
	public void fillData(JSONObject msg) throws ClassNotFoundException{

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
'
			result+='				
		if(msg.get("topic") == "' + context.name + '"){
'
			for (FunctionblockProperty fbp : context.getProperties()) {	
				index++;
				fbpNames.add(fbp.name)
				result+=fillFunctionBlock(fbp,context,index-1)
				result+='
			stmt.executeUpdate(FB'+(index-1)+');
'
			}
			result+='
			String IM = "";'	
			result+=fillInformationModel(context, fbpNames)
			result+='
			stmt.executeUpdate(IM);'
			result+='
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
	}'
			

		
			
			return result
		}
	
	def fillFunctionBlock(FunctionblockProperty fbp, InformationModel context, int index) {
		var result=''
		var fbresult=''
		var eresult=''
		var eindex=0
		var attributeList = new ArrayList<String>
		var oattributeNameList = new ArrayList<String>
		var oattributeList = new BasicEList<Type>
		var eattributeNameList = new ArrayList<String>
		var eattributeList = new BasicEList<Type>
		var hierarchy = new ArrayList<String>
		hierarchy.add(fbp.name)
				
		if (fbp.getType().getFunctionblock().status != null) {			
			for (content : fbp.getType().getFunctionblock().status.properties) {
				if (content.type instanceof PrimitivePropertyType) {
					attributeList.add(content.name)
				}
				if (content.type instanceof ObjectPropertyType) {
					var objectType = content.getType() as ObjectPropertyType
					if(objectType.type instanceof Enum){
						eattributeNameList.add(content.name)
						eattributeList.add(objectType.type)					
					}
					else{
						oattributeNameList.add(content.name)
						oattributeList.add(objectType.type)
						eresult += fillEntity(objectType.type as Entity, content.name, hierarchy, eindex, index)
						eindex++
					}				
				}
			}
		}
		if (fbp.getType().getFunctionblock().fault != null) {
			for (content : fbp.getType().getFunctionblock().fault.properties) {
				if (content.type instanceof PrimitivePropertyType) {
					attributeList.add(content.name)
				}
				if (content.type instanceof ObjectPropertyType) {
					var objectType = content.getType() as ObjectPropertyType
					if(objectType.type instanceof Enum){
						eattributeNameList.add(content.name)
						eattributeList.add(objectType.type)								
					}
					else{
						oattributeNameList.add(content.name)
						oattributeList.add(objectType.type)
						eresult += fillEntity(objectType.type as Entity, content.name, hierarchy, eindex, index)
						eindex++
					}	
				}
			}		
		}
		if (fbp.getType().getFunctionblock().configuration != null) {
			for (content : fbp.getType().getFunctionblock().configuration.properties) {
				if (content.type instanceof PrimitivePropertyType) {
					attributeList.add(content.name)
				}
				if (content.type instanceof ObjectPropertyType) {
					var objectType = content.getType() as ObjectPropertyType
					if(objectType.type instanceof Enum){
						eattributeNameList.add(content.name)
						eattributeList.add(objectType.type)								
					}
					else{
						oattributeNameList.add(content.name)
						oattributeList.add(objectType.type)
						eresult += fillEntity(objectType.type as Entity, content.name, hierarchy, eindex, index)
						eindex++
					}	
				}
			}
		}
		if (fbp.getType().getFunctionblock().events != null) {
			for (content : fbp.getType().getFunctionblock().events) {
				if (content instanceof PrimitivePropertyType) {
					attributeList.add(content.name)
				}
				if (content instanceof ObjectPropertyType) {
					var objectType = content.getType() as ObjectPropertyType
					if(objectType.type instanceof Enum){
						eattributeNameList.add(content.name)
						eattributeList.add(objectType.type)								
					}
					else{
						oattributeNameList.add(content.name)
						oattributeList.add(objectType.type)
						eresult += fillEntity(objectType.type as Entity, content.name, hierarchy, eindex, index)
						eindex++
					}	
				}
			}		
		}
		if(!attributeList.isEmpty || !oattributeList.isEmpty || !eattributeList.isEmpty){
		fbresult+= '
			String FB'+ index +'= "";'
		fbresult+='
			FB'+ index + ' += "INSERT INTO '+ fbp.name + ' ('
		for (attribute : attributeList){
			fbresult+=attribute+','
		}
		for (attribute : eattributeNameList){
			fbresult+=attribute+','
		}
		for (attribute : oattributeNameList){
			fbresult+=attribute+','
		}
		fbresult = fbresult.substring(0, fbresult.length()-1);
		fbresult+= ') VALUES ( ";'
		for (attribute : attributeList){
			fbresult+='
			FB'+ index + ' += msg.getJSONObject("payload").getJSONObject("' + fbp.name + '").get("' + attribute + '") + ",";'
		}	
		for (attribute : eattributeList){
			fbresult+='
			FB'+ index + ' += "(SELECT p_key FROM ' + attribute.name + ' WHERE enums =" + msg.getJSONObject("payload").getJSONObject("' + fbp.name + '").get("' + attribute.name + '") + "),";'
		}
		for (attribute : oattributeList){
			fbresult+='
			FB'+ index + ' += "(SELECT MAX(p_key) FROM ' + attribute.name + '),";'
		}
		fbresult = fbresult.substring(0, fbresult.length()-3);	
		fbresult+='";
			FB'+ index + ' += ")";'
		}
		return result+=eresult+fbresult
	}
	
	def fillEntity(Type entity, String type, ArrayList<String> hierarchy, int index, int fbindex) {
		var entityType = entity as Entity
		var attributeList = new ArrayList<String>
		var eattributeNameList = new ArrayList<String>
		var eattributeList = new BasicEList<Type>
		var oattributeNameList = new ArrayList<String>
		var oattributeList = new BasicEList<Type>
		var result=''
		var nexthierarchy = new ArrayList<String>
		var currenthierarchy = new ArrayList<String>
		for(element : hierarchy){
			currenthierarchy.add(element)
		}
		currenthierarchy.add(type)
		for(element : currenthierarchy){
			nexthierarchy.add(element)
		}
		
		for (Property attribute : entityType.getProperties()) {
			if(attribute.type instanceof ObjectPropertyType){
			var objectType = attribute.getType() as ObjectPropertyType	
				if(objectType.type instanceof Entity){
					oattributeNameList.add(attribute.name)
					oattributeList.add(objectType.type)
					result+=fillEntity(objectType.type as Entity, attribute.name, nexthierarchy, index+1, fbindex)
				}
				if(objectType.type instanceof Enum){
					eattributeNameList.add(attribute.name)
					eattributeList.add(objectType.type)
				}
			}else{
				attributeList.add(attribute.name)
			}
		}
		
		result+='
			String EN'+fbindex+index+'= "";'+'
			EN'+fbindex+index+' += "INSERT INTO '+ entity.name + ' ('
		for(attribute : attributeList){
			result+=attribute + ','	
		}
		for(attribute : eattributeNameList){
			result+=attribute + ','	
		}
		for(attribute : oattributeNameList){
			result+=attribute + ','	
		}
		result = result.substring(0, result.length()-1);
		result+=') VALUES ( ";'	
		for(attribute : attributeList){
			result+='		
			EN'+fbindex+index+' += msg.getJSONObject("payload").'
			for(element : currenthierarchy){
				result+='getJSONObject("'+ element+ '").'	
			}
			result+= 'get("' + attribute + '") + ",";'
		}
		for(attribute : eattributeList){
			result+='		
			EN'+fbindex+index+' += "(SELECT p_key FROM ' + attribute.name + ' WHERE enums =" + msg.getJSONObject("payload")'
			for(element : currenthierarchy){
				result+='getJSONObject("'+ element+ '").'	
			}
			result+='get("' + attribute.name + '") + " ),";'
		}
		for (attribute : oattributeList){
			result+='
			EN'+fbindex+index+ ' += "(SELECT MAX(p_key) FROM ' + attribute.name + '),";'
		}
		result = result.substring(0, result.length()-3);
		result+='";
			EN'+fbindex+index+' += ")";'
		result+='
			stmt.executeUpdate(EN'+fbindex+index+');
'
		return result
	}
	
	def fillInformationModel(InformationModel context, ArrayList<String> fbplist) {
		var result=''		
		result+='
			IM += "INSERT INTO '+ context.name + " ("	
		for(fb : fbplist){
			result+=fb + ","
		}
		result+='measure_time) VALUES ( ";'
		for(fb : fbplist){
		result+='
			IM += "(SELECT MAX(p_key) FROM ' + fb + '),";'
		}
		result+='
			IM += msg.getJSONObject("payload").get("measure_time");'
		result+='
			IM += ")";' 		
		return result
	}
	
	}

	override getServiceKey() {
		return "myVortoGenerator";
	}
}
