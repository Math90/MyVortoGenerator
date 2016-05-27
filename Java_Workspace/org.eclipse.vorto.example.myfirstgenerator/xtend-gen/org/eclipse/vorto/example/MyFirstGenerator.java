package org.eclipse.vorto.example;

import com.google.common.base.Objects;
import java.util.ArrayList;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IFileTemplate;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IMappingContext;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.datatype.PropertyType;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.Fault;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Status;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

@SuppressWarnings("all")
public class MyFirstGenerator implements IVortoCodeGenerator {
  public static class SQLTemplate implements IFileTemplate<InformationModel> {
    private ArrayList<String> tableList = new ArrayList<String>();
    
    @Override
    public String getFileName(final InformationModel context) {
      return "creator.sql";
    }
    
    @Override
    public String getPath(final InformationModel context) {
      return "sql";
    }
    
    @Override
    public String getContent(final InformationModel context) {
      String result = "";
      String maindata = "";
      String _name = context.getName();
      this.tableList.add(_name);
      String _maindata = maindata;
      String _name_1 = context.getName();
      String _plus = ("CREATE TABLE " + _name_1);
      String _plus_1 = (_plus + ";");
      String _property = System.getProperty("line.separator");
      String _plus_2 = (_plus_1 + _property);
      maindata = (_maindata + _plus_2);
      String _maindata_1 = maindata;
      String _name_2 = context.getName();
      String _plus_3 = ("ALTER TABLE " + _name_2);
      String _property_1 = System.getProperty("line.separator");
      String _plus_4 = (_plus_3 + _property_1);
      maindata = (_maindata_1 + _plus_4);
      EList<FunctionblockProperty> _properties = context.getProperties();
      for (final FunctionblockProperty fbp : _properties) {
        {
          FunctionblockModel _type = fbp.getType();
          String _name_3 = _type.getName();
          this.tableList.add(_name_3);
          String _maindata_2 = maindata;
          String _name_4 = fbp.getName();
          String _plus_5 = ("ADD " + _name_4);
          String _plus_6 = (_plus_5 + " INTEGER NOT NULL ");
          String _property_2 = System.getProperty("line.separator");
          String _plus_7 = (_plus_6 + _property_2);
          maindata = (_maindata_2 + _plus_7);
          String _maindata_3 = maindata;
          String _name_5 = fbp.getName();
          String _plus_8 = ("ADD FOREIGN KEY(" + _name_5);
          String _plus_9 = (_plus_8 + ")");
          String _property_3 = System.getProperty("line.separator");
          String _plus_10 = (_plus_9 + _property_3);
          maindata = (_maindata_3 + _plus_10);
          String _maindata_4 = maindata;
          FunctionblockModel _type_1 = fbp.getType();
          String _name_6 = _type_1.getName();
          String _plus_11 = ("REFERENCES " + _name_6);
          String _plus_12 = (_plus_11 + "(p_key)");
          String _property_4 = System.getProperty("line.separator");
          String _plus_13 = (_plus_12 + _property_4);
          maindata = (_maindata_4 + _plus_13);
          FunctionblockModel _type_2 = fbp.getType();
          FunctionBlock fb = _type_2.getFunctionblock();
          BasicEList<Type> _referencedEnumsAndEntities = this.getReferencedEnumsAndEntities(fb);
          for (final Type type : _referencedEnumsAndEntities) {
            {
              if ((type instanceof Entity)) {
                String _result = result;
                String _generateForEntity = this.generateForEntity(((Entity)type));
                result = (_result + _generateForEntity);
              }
              if ((type instanceof org.eclipse.vorto.core.api.model.datatype.Enum)) {
                String _result_1 = result;
                String _generateForEnum = this.generateForEnum(((org.eclipse.vorto.core.api.model.datatype.Enum)type));
                result = (_result_1 + _generateForEnum);
              }
            }
          }
          String _result = result;
          FunctionblockModel _type_3 = fbp.getType();
          String _name_7 = _type_3.getName();
          String _plus_14 = ("CREATE TABLE " + _name_7);
          String _plus_15 = (_plus_14 + ";");
          String _property_5 = System.getProperty("line.separator");
          String _plus_16 = (_plus_15 + _property_5);
          result = (_result + _plus_16);
          String _result_1 = result;
          String _name_8 = fbp.getName();
          String _plus_17 = ("ALTER TABLE " + _name_8);
          String _property_6 = System.getProperty("line.separator");
          String _plus_18 = (_plus_17 + _property_6);
          result = (_result_1 + _plus_18);
          FunctionblockModel _type_4 = fbp.getType();
          FunctionBlock _functionblock = _type_4.getFunctionblock();
          Status _status = _functionblock.getStatus();
          boolean _notEquals = (!Objects.equal(_status, null));
          if (_notEquals) {
            String _result_2 = result;
            FunctionblockModel _type_5 = fbp.getType();
            FunctionBlock _functionblock_1 = _type_5.getFunctionblock();
            String _generateFunctionblockStatus = this.generateFunctionblockStatus(_functionblock_1);
            result = (_result_2 + _generateFunctionblockStatus);
          }
          FunctionblockModel _type_6 = fbp.getType();
          FunctionBlock _functionblock_2 = _type_6.getFunctionblock();
          Fault _fault = _functionblock_2.getFault();
          boolean _notEquals_1 = (!Objects.equal(_fault, null));
          if (_notEquals_1) {
            String _result_3 = result;
            FunctionblockModel _type_7 = fbp.getType();
            FunctionBlock _functionblock_3 = _type_7.getFunctionblock();
            String _generateFunctionblockFault = this.generateFunctionblockFault(_functionblock_3);
            result = (_result_3 + _generateFunctionblockFault);
          }
          FunctionblockModel _type_8 = fbp.getType();
          FunctionBlock _functionblock_4 = _type_8.getFunctionblock();
          Configuration _configuration = _functionblock_4.getConfiguration();
          boolean _notEquals_2 = (!Objects.equal(_configuration, null));
          if (_notEquals_2) {
            String _result_4 = result;
            FunctionblockModel _type_9 = fbp.getType();
            FunctionBlock _functionblock_5 = _type_9.getFunctionblock();
            String _generateFunctionblockConfiguration = this.generateFunctionblockConfiguration(_functionblock_5);
            result = (_result_4 + _generateFunctionblockConfiguration);
          }
          FunctionblockModel _type_10 = fbp.getType();
          FunctionBlock _functionblock_6 = _type_10.getFunctionblock();
          EList<Event> _events = _functionblock_6.getEvents();
          boolean _notEquals_3 = (!Objects.equal(_events, null));
          if (_notEquals_3) {
            String _result_5 = result;
            FunctionblockModel _type_11 = fbp.getType();
            FunctionBlock _functionblock_7 = _type_11.getFunctionblock();
            String _generateFunctionblockEvents = this.generateFunctionblockEvents(_functionblock_7);
            result = (_result_5 + _generateFunctionblockEvents);
          }
          String _result_6 = result;
          String _property_7 = System.getProperty("line.separator");
          String _plus_19 = ("ADD p_key INTEGER NOT NULL AUTO_INCREMENT" + _property_7);
          result = (_result_6 + _plus_19);
          String _result_7 = result;
          String _property_8 = System.getProperty("line.separator");
          String _plus_20 = ("ADD PRIMARY KEY (p_key)" + _property_8);
          result = (_result_7 + _plus_20);
          String _result_8 = result;
          String _property_9 = System.getProperty("line.separator");
          String _plus_21 = (";" + _property_9);
          String _property_10 = System.getProperty("line.separator");
          String _plus_22 = (_plus_21 + _property_10);
          result = (_result_8 + _plus_22);
        }
      }
      String _maindata_2 = maindata;
      maindata = (_maindata_2 + "ADD measure_time TIME;");
      String _result = result;
      result = (_result + maindata);
      return result;
    }
    
    public String generateForEnum(final org.eclipse.vorto.core.api.model.datatype.Enum enum1) {
      String result = "";
      int index = 0;
      String _result = result;
      String _name = enum1.getName();
      String _plus = ("CREATE TABLE " + _name);
      String _property = System.getProperty("line.separator");
      String _plus_1 = (_plus + _property);
      result = (_result + _plus_1);
      String _result_1 = result;
      String _property_1 = System.getProperty("line.separator");
      String _plus_2 = ("(" + _property_1);
      String _plus_3 = (_plus_2 + "enums VARCHAR(20),");
      String _property_2 = System.getProperty("line.separator");
      String _plus_4 = (_plus_3 + _property_2);
      String _plus_5 = (_plus_4 + "p_key INTEGER NOT NULL,");
      String _property_3 = System.getProperty("line.separator");
      String _plus_6 = (_plus_5 + _property_3);
      String _plus_7 = (_plus_6 + "PRIMARY KEY (p_key)");
      String _property_4 = System.getProperty("line.separator");
      String _plus_8 = (_plus_7 + _property_4);
      String _plus_9 = (_plus_8 + ");");
      String _property_5 = System.getProperty("line.separator");
      String _plus_10 = (_plus_9 + _property_5);
      result = (_result_1 + _plus_10);
      String _result_2 = result;
      String _name_1 = enum1.getName();
      String _plus_11 = ("INSERT INTO " + _name_1);
      String _plus_12 = (_plus_11 + " (enums,p_key)");
      String _property_6 = System.getProperty("line.separator");
      String _plus_13 = (_plus_12 + _property_6);
      String _plus_14 = (_plus_13 + "VALUES (");
      String _property_7 = System.getProperty("line.separator");
      String _plus_15 = (_plus_14 + _property_7);
      result = (_result_2 + _plus_15);
      EList<EnumLiteral> _enums = enum1.getEnums();
      for (final EnumLiteral content : _enums) {
        {
          String _result_3 = result;
          String _name_2 = content.getName();
          String _plus_16 = ("(" + _name_2);
          String _plus_17 = (_plus_16 + " , ");
          String _plus_18 = (_plus_17 + Integer.valueOf(index));
          String _plus_19 = (_plus_18 + "),");
          String _property_8 = System.getProperty("line.separator");
          String _plus_20 = (_plus_19 + _property_8);
          result = (_result_3 + _plus_20);
          index++;
        }
      }
      String _result_3 = result;
      String _property_8 = System.getProperty("line.separator");
      String _plus_16 = (");" + _property_8);
      String _property_9 = System.getProperty("line.separator");
      String _plus_17 = (_plus_16 + _property_9);
      result = (_result_3 + _plus_17);
      return result;
    }
    
    public BasicEList<Type> getReferencedEnumsAndEntities(final FunctionBlock fb) {
      BasicEList<Type> list = new BasicEList<Type>();
      BasicEList<Type> _referencedTypesFB = this.getReferencedTypesFB(fb);
      for (final Type type : _referencedTypesFB) {
        boolean _contains = list.contains(type);
        boolean _not = (!_contains);
        if (_not) {
          list.add(((Type) type));
        }
      }
      return list;
    }
    
    public String generateForEntity(final Entity entity) {
      String result = "";
      String _result = result;
      String _name = entity.getName();
      String _plus = ("CREATE TABLE " + _name);
      String _plus_1 = (_plus + ";");
      String _property = System.getProperty("line.separator");
      String _plus_2 = (_plus_1 + _property);
      result = (_result + _plus_2);
      String _result_1 = result;
      String _name_1 = entity.getName();
      String _plus_3 = ("ALTER TABLE " + _name_1);
      String _property_1 = System.getProperty("line.separator");
      String _plus_4 = (_plus_3 + _property_1);
      result = (_result_1 + _plus_4);
      EList<Property> _properties = entity.getProperties();
      for (final Property content : _properties) {
        {
          String _result_2 = result;
          String _name_2 = content.getName();
          String _plus_5 = ("ADD " + _name_2);
          String _plus_6 = (_plus_5 + " ");
          result = (_result_2 + _plus_6);
          PropertyType _type = content.getType();
          if ((_type instanceof PrimitivePropertyType)) {
            String _result_3 = result;
            PropertyType _type_1 = content.getType();
            String _mapPrimitiveType = this.mapPrimitiveType(((PrimitivePropertyType) _type_1));
            String _property_2 = System.getProperty("line.separator");
            String _plus_7 = (_mapPrimitiveType + _property_2);
            result = (_result_3 + _plus_7);
          }
          PropertyType _type_2 = content.getType();
          if ((_type_2 instanceof ObjectPropertyType)) {
            String _result_4 = result;
            String _property_3 = System.getProperty("line.separator");
            String _plus_8 = ("INTEGER NOT NULL" + _property_3);
            result = (_result_4 + _plus_8);
            String _result_5 = result;
            String _name_3 = content.getName();
            String _plus_9 = ("ADD FOREIGN KEY (" + _name_3);
            String _plus_10 = (_plus_9 + ")");
            String _property_4 = System.getProperty("line.separator");
            String _plus_11 = (_plus_10 + _property_4);
            result = (_result_5 + _plus_11);
            String _result_6 = result;
            result = (_result_6 + "REFERENCES ");
            String _result_7 = result;
            PropertyType _type_3 = content.getType();
            String _mapOjectType = this.mapOjectType(_type_3);
            String _plus_12 = (_mapOjectType + "(p_key)");
            String _property_5 = System.getProperty("line.separator");
            String _plus_13 = (_plus_12 + _property_5);
            result = (_result_7 + _plus_13);
          }
        }
      }
      String _result_2 = result;
      String _property_2 = System.getProperty("line.separator");
      String _plus_5 = ("ADD p_key INTEGER NOT NULL AUTO_INCREMENT" + _property_2);
      result = (_result_2 + _plus_5);
      String _result_3 = result;
      String _property_3 = System.getProperty("line.separator");
      String _plus_6 = ("ADD PRIMARY KEY (p_key)" + _property_3);
      result = (_result_3 + _plus_6);
      String _result_4 = result;
      String _property_4 = System.getProperty("line.separator");
      String _plus_7 = (";" + _property_4);
      String _property_5 = System.getProperty("line.separator");
      String _plus_8 = (_plus_7 + _property_5);
      result = (_result_4 + _plus_8);
      return result;
    }
    
    public BasicEList<Type> getReferencedTypesFB(final FunctionBlock fb) {
      BasicEList<Type> types = new BasicEList<Type>();
      boolean _notEquals = (!Objects.equal(fb, null));
      if (_notEquals) {
        Status _status = fb.getStatus();
        boolean _notEquals_1 = (!Objects.equal(_status, null));
        if (_notEquals_1) {
          Status _status_1 = fb.getStatus();
          EList<Property> _properties = _status_1.getProperties();
          for (final Property property : _properties) {
            BasicEList<Type> _referencedTypesP = this.getReferencedTypesP(property);
            types.addAll(_referencedTypesP);
          }
        }
        Configuration _configuration = fb.getConfiguration();
        boolean _notEquals_2 = (!Objects.equal(_configuration, null));
        if (_notEquals_2) {
          Configuration _configuration_1 = fb.getConfiguration();
          EList<Property> _properties_1 = _configuration_1.getProperties();
          for (final Property property_1 : _properties_1) {
            BasicEList<Type> _referencedTypesP_1 = this.getReferencedTypesP(property_1);
            types.addAll(_referencedTypesP_1);
          }
        }
        Fault _fault = fb.getFault();
        boolean _notEquals_3 = (!Objects.equal(_fault, null));
        if (_notEquals_3) {
          Fault _fault_1 = fb.getFault();
          EList<Property> _properties_2 = _fault_1.getProperties();
          for (final Property property_2 : _properties_2) {
            BasicEList<Type> _referencedTypesP_2 = this.getReferencedTypesP(property_2);
            types.addAll(_referencedTypesP_2);
          }
        }
      }
      return types;
    }
    
    public BasicEList<Type> getReferencedTypesP(final Property property) {
      BasicEList<Type> types = new BasicEList<Type>();
      PropertyType _type = property.getType();
      if ((_type instanceof ObjectPropertyType)) {
        PropertyType _type_1 = property.getType();
        ObjectPropertyType objectType = ((ObjectPropertyType) _type_1);
        Type _type_2 = objectType.getType();
        String _name = _type_2.getName();
        boolean _contains = this.tableList.contains(_name);
        boolean _not = (!_contains);
        if (_not) {
          Type _type_3 = objectType.getType();
          String _name_1 = _type_3.getName();
          this.tableList.add(_name_1);
          Type _type_4 = objectType.getType();
          types.add(_type_4);
          Type _type_5 = objectType.getType();
          if ((_type_5 instanceof Entity)) {
            Type _type_6 = objectType.getType();
            EList<Type> _referencedTypesT = this.getReferencedTypesT(((Entity) _type_6));
            types.addAll(_referencedTypesT);
          }
        }
      }
      return types;
    }
    
    public EList<Type> getReferencedTypesT(final Type type) {
      EList<Type> types = new BasicEList<Type>();
      if ((type instanceof Entity)) {
        Entity entityType = ((Entity) type);
        EList<Property> _properties = entityType.getProperties();
        for (final Property property : _properties) {
          Object _referencedTypesP = this.getReferencedTypesP(property);
          types.addAll(((BasicEList<Type>) _referencedTypesP));
        }
        Entity _superType = entityType.getSuperType();
        types.add(_superType);
      }
      return types;
    }
    
    public String generateFunctionblockEvents(final FunctionBlock block) {
      String result = "";
      EList<Event> _events = block.getEvents();
      for (final Event content : _events) {
        {
          if ((content instanceof PrimitivePropertyType)) {
            String _result = result;
            String _name = content.getName();
            String _plus = ("ADD " + _name);
            String _plus_1 = (_plus + " ");
            PrimitiveType _type = ((PrimitivePropertyType)content).getType();
            String _name_1 = _type.getName();
            String _plus_2 = (_plus_1 + _name_1);
            String _property = System.getProperty("line.separator");
            String _plus_3 = (_plus_2 + _property);
            result = (_result + _plus_3);
          }
          if ((content instanceof ObjectPropertyType)) {
            String _result_1 = result;
            String _name_2 = content.getName();
            String _plus_4 = ("ADD " + _name_2);
            String _plus_5 = (_plus_4 + " INTEGER NOT NULL");
            String _property_1 = System.getProperty("line.separator");
            String _plus_6 = (_plus_5 + _property_1);
            result = (_result_1 + _plus_6);
            String _result_2 = result;
            String _name_3 = content.getName();
            String _plus_7 = ("ADD FOREIGN KEY (" + _name_3);
            String _plus_8 = (_plus_7 + ")");
            String _property_2 = System.getProperty("line.separator");
            String _plus_9 = (_plus_8 + _property_2);
            result = (_result_2 + _plus_9);
            String _result_3 = result;
            result = (_result_3 + "REFERENCES ");
            String _result_4 = result;
            String _mapOjectType = this.mapOjectType(((ObjectPropertyType)content));
            String _plus_10 = (_mapOjectType + "(p_key)");
            String _property_3 = System.getProperty("line.separator");
            String _plus_11 = (_plus_10 + _property_3);
            result = (_result_4 + _plus_11);
          }
        }
      }
      return result;
    }
    
    public String generateFunctionblockConfiguration(final FunctionBlock block) {
      String result = "";
      Configuration _configuration = block.getConfiguration();
      EList<Property> _properties = _configuration.getProperties();
      for (final Property content : _properties) {
        {
          String _result = result;
          String _name = content.getName();
          String _plus = ("ADD " + _name);
          String _plus_1 = (_plus + " ");
          result = (_result + _plus_1);
          PropertyType _type = content.getType();
          if ((_type instanceof PrimitivePropertyType)) {
            String _result_1 = result;
            PropertyType _type_1 = content.getType();
            String _mapPrimitiveType = this.mapPrimitiveType(((PrimitivePropertyType) _type_1));
            String _property = System.getProperty("line.separator");
            String _plus_2 = (_mapPrimitiveType + _property);
            result = (_result_1 + _plus_2);
          }
          PropertyType _type_2 = content.getType();
          if ((_type_2 instanceof ObjectPropertyType)) {
            String _result_2 = result;
            String _property_1 = System.getProperty("line.separator");
            String _plus_3 = ("INTEGER NOT NULL" + _property_1);
            result = (_result_2 + _plus_3);
            String _result_3 = result;
            String _name_1 = content.getName();
            String _plus_4 = ("ADD FOREIGN KEY (" + _name_1);
            String _plus_5 = (_plus_4 + ")");
            String _property_2 = System.getProperty("line.separator");
            String _plus_6 = (_plus_5 + _property_2);
            result = (_result_3 + _plus_6);
            String _result_4 = result;
            result = (_result_4 + "REFERENCES ");
            String _result_5 = result;
            PropertyType _type_3 = content.getType();
            String _mapOjectType = this.mapOjectType(_type_3);
            String _plus_7 = (_mapOjectType + "(p_key)");
            String _property_3 = System.getProperty("line.separator");
            String _plus_8 = (_plus_7 + _property_3);
            result = (_result_5 + _plus_8);
          }
        }
      }
      return result;
    }
    
    public String generateFunctionblockFault(final FunctionBlock block) {
      String result = "";
      Fault _fault = block.getFault();
      EList<Property> _properties = _fault.getProperties();
      for (final Property content : _properties) {
        {
          String _result = result;
          String _name = content.getName();
          String _plus = ("ADD " + _name);
          String _plus_1 = (_plus + " ");
          result = (_result + _plus_1);
          PropertyType _type = content.getType();
          if ((_type instanceof PrimitivePropertyType)) {
            String _result_1 = result;
            PropertyType _type_1 = content.getType();
            String _mapPrimitiveType = this.mapPrimitiveType(((PrimitivePropertyType) _type_1));
            String _property = System.getProperty("line.separator");
            String _plus_2 = (_mapPrimitiveType + _property);
            result = (_result_1 + _plus_2);
          }
          PropertyType _type_2 = content.getType();
          if ((_type_2 instanceof ObjectPropertyType)) {
            String _result_2 = result;
            String _property_1 = System.getProperty("line.separator");
            String _plus_3 = ("INTEGER NOT NULL" + _property_1);
            result = (_result_2 + _plus_3);
            String _result_3 = result;
            String _name_1 = content.getName();
            String _plus_4 = ("ADD FOREIGN KEY (" + _name_1);
            String _plus_5 = (_plus_4 + ")");
            String _property_2 = System.getProperty("line.separator");
            String _plus_6 = (_plus_5 + _property_2);
            result = (_result_3 + _plus_6);
            String _result_4 = result;
            result = (_result_4 + "REFERENCES ");
            String _result_5 = result;
            PropertyType _type_3 = content.getType();
            String _mapOjectType = this.mapOjectType(_type_3);
            String _plus_7 = (_mapOjectType + "(p_key)");
            String _property_3 = System.getProperty("line.separator");
            String _plus_8 = (_plus_7 + _property_3);
            result = (_result_5 + _plus_8);
          }
        }
      }
      return result;
    }
    
    public String generateFunctionblockStatus(final FunctionBlock block) {
      String result = "";
      Status _status = block.getStatus();
      EList<Property> _properties = _status.getProperties();
      for (final Property content : _properties) {
        {
          String _result = result;
          String _name = content.getName();
          String _plus = ("ADD " + _name);
          String _plus_1 = (_plus + " ");
          result = (_result + _plus_1);
          PropertyType _type = content.getType();
          if ((_type instanceof PrimitivePropertyType)) {
            String _result_1 = result;
            PropertyType _type_1 = content.getType();
            String _mapPrimitiveType = this.mapPrimitiveType(((PrimitivePropertyType) _type_1));
            String _property = System.getProperty("line.separator");
            String _plus_2 = (_mapPrimitiveType + _property);
            result = (_result_1 + _plus_2);
          }
          PropertyType _type_2 = content.getType();
          if ((_type_2 instanceof ObjectPropertyType)) {
            String _result_2 = result;
            String _property_1 = System.getProperty("line.separator");
            String _plus_3 = ("INTEGER NOT NULL" + _property_1);
            result = (_result_2 + _plus_3);
            String _result_3 = result;
            String _name_1 = content.getName();
            String _plus_4 = ("ADD FOREIGN KEY (" + _name_1);
            String _plus_5 = (_plus_4 + ")");
            String _property_2 = System.getProperty("line.separator");
            String _plus_6 = (_plus_5 + _property_2);
            result = (_result_3 + _plus_6);
            String _result_4 = result;
            result = (_result_4 + "REFERENCES ");
            String _result_5 = result;
            PropertyType _type_3 = content.getType();
            String _mapOjectType = this.mapOjectType(_type_3);
            String _plus_7 = (_mapOjectType + "(p_key)");
            String _property_3 = System.getProperty("line.separator");
            String _plus_8 = (_plus_7 + _property_3);
            result = (_result_5 + _plus_8);
          }
        }
      }
      return result;
    }
    
    public String mapPrimitiveType(final PrimitivePropertyType type) {
      String result = "";
      PrimitiveType _type = type.getType();
      String _name = _type.getName();
      boolean _equals = Objects.equal(_name, "boolean");
      if (_equals) {
        String _result = result;
        result = (_result + "BOOLEAN");
      } else {
        PrimitiveType _type_1 = type.getType();
        String _name_1 = _type_1.getName();
        boolean _equals_1 = Objects.equal(_name_1, "int");
        if (_equals_1) {
          String _result_1 = result;
          result = (_result_1 + "INTEGER");
        } else {
          PrimitiveType _type_2 = type.getType();
          String _name_2 = _type_2.getName();
          boolean _equals_2 = Objects.equal(_name_2, "double");
          if (_equals_2) {
            String _result_2 = result;
            result = (_result_2 + "DOUBLE PRECISION");
          } else {
            PrimitiveType _type_3 = type.getType();
            String _name_3 = _type_3.getName();
            boolean _equals_3 = Objects.equal(_name_3, "datetime");
            if (_equals_3) {
              String _result_3 = result;
              result = (_result_3 + "TIMESTAMP");
            } else {
              PrimitiveType _type_4 = type.getType();
              String _name_4 = _type_4.getName();
              boolean _equals_4 = Objects.equal(_name_4, "float");
              if (_equals_4) {
                String _result_4 = result;
                result = (_result_4 + "FLOAT");
              } else {
                PrimitiveType _type_5 = type.getType();
                String _name_5 = _type_5.getName();
                boolean _equals_5 = Objects.equal(_name_5, "string");
                if (_equals_5) {
                  String _result_5 = result;
                  result = (_result_5 + "CHARACTER(20)");
                } else {
                  PrimitiveType _type_6 = type.getType();
                  String _name_6 = _type_6.getName();
                  boolean _equals_6 = Objects.equal(_name_6, "long");
                  if (_equals_6) {
                    String _result_6 = result;
                    result = (_result_6 + "BIGINT");
                  } else {
                    PrimitiveType _type_7 = type.getType();
                    String _name_7 = _type_7.getName();
                    boolean _equals_7 = Objects.equal(_name_7, "short");
                    if (_equals_7) {
                      String _result_7 = result;
                      result = (_result_7 + "SMALLINT");
                    }
                  }
                }
              }
            }
          }
        }
      }
      return result;
    }
    
    public String mapOjectType(final PropertyType type) {
      String result = "";
      ObjectPropertyType object = ((ObjectPropertyType) type);
      Type _type = object.getType();
      if ((_type instanceof Entity)) {
        String _result = result;
        Type _type_1 = object.getType();
        String _name = ((Entity) _type_1).getName();
        result = (_result + _name);
      } else {
        Type _type_2 = object.getType();
        if ((_type_2 instanceof org.eclipse.vorto.core.api.model.datatype.Enum)) {
          String _result_1 = result;
          Type _type_3 = object.getType();
          String _name_1 = ((org.eclipse.vorto.core.api.model.datatype.Enum) _type_3).getName();
          result = (_result_1 + _name_1);
        }
      }
      return result;
    }
  }
  
  public static class JavaTemplate implements IFileTemplate<InformationModel> {
    @Override
    public String getFileName(final InformationModel context) {
      return "MQTTfiller.java";
    }
    
    @Override
    public String getPath(final InformationModel context) {
      return "java";
    }
    
    @Override
    public String getContent(final InformationModel context) {
      String result = "";
      int index = 0;
      ArrayList<String> fbpNames = new ArrayList<String>();
      String _result = result;
      result = (_result + "\r\n\tpublic void fillData(JSONObject msg) throws ClassNotFoundException{\r\n\r\n\t\t// JDBC driver name and database URL\r\n\t\tString JDBC_DRIVER = \"$JDBCDriver\";  \r\n\t\tString DB_URL = \"$JDBCURL\";\r\n\r\n\t\t//  Database credentials\r\n\t\tString USER = \"$username\";\r\n\t\tString PASS = \"$password\";\r\n\r\n\t\tConnection conn = null;\r\n\t\tStatement stmt = null;\r\n\t\t\r\n\ttry{\r\n\r\n\t\tClass.forName(JDBC_DRIVER);\r\n\t\tconn = DriverManager.getConnection(DB_URL, USER, PASS);\r\n\t\tstmt = conn.createStatement();\r\n");
      String _result_1 = result;
      String _name = context.getName();
      String _plus = ("\t\t\t\t\r\n\t\tif(msg.get(\"topic\") == \"" + _name);
      String _plus_1 = (_plus + "\"){\r\n");
      result = (_result_1 + _plus_1);
      EList<FunctionblockProperty> _properties = context.getProperties();
      for (final FunctionblockProperty fbp : _properties) {
        {
          index++;
          String _name_1 = fbp.getName();
          fbpNames.add(_name_1);
          String _result_2 = result;
          String _fillFunctionBlock = this.fillFunctionBlock(fbp, context, (index - 1));
          result = (_result_2 + _fillFunctionBlock);
          String _result_3 = result;
          result = (_result_3 + (("\r\n\t\t\tstmt.executeUpdate(FB" + Integer.valueOf((index - 1))) + ");\r\n"));
        }
      }
      String _result_2 = result;
      result = (_result_2 + "\r\n\t\t\tString IM = \"\";");
      String _result_3 = result;
      String _fillInformationModel = this.fillInformationModel(context, fbpNames);
      result = (_result_3 + _fillInformationModel);
      String _result_4 = result;
      result = (_result_4 + "\r\n\t\t\tstmt.executeUpdate(IM);");
      String _result_5 = result;
      result = (_result_5 + "\r\n\t\t}\r\n\t}\r\n\r\n\tcatch(SQLException se){se.printStackTrace();}\r\n\tcatch(Exception e){e.printStackTrace();}\r\n\tfinally{\r\n\t\ttry{if(stmt!=null) conn.close();}\r\n\t\tcatch(SQLException se){}\r\n\t\ttry{if(conn!=null) conn.close();}\r\n\t\tcatch(SQLException se){se.printStackTrace();}\r\n\t\t}\r\n\t}");
      return result;
    }
    
    public String fillFunctionBlock(final FunctionblockProperty fbp, final InformationModel context, final int index) {
      String result = "";
      String fbresult = "";
      String eresult = "";
      int eindex = 0;
      ArrayList<String> attributeList = new ArrayList<String>();
      ArrayList<String> oattributeNameList = new ArrayList<String>();
      BasicEList<Type> oattributeList = new BasicEList<Type>();
      ArrayList<String> eattributeNameList = new ArrayList<String>();
      BasicEList<Type> eattributeList = new BasicEList<Type>();
      ArrayList<String> hierarchy = new ArrayList<String>();
      String _name = fbp.getName();
      hierarchy.add(_name);
      FunctionblockModel _type = fbp.getType();
      FunctionBlock _functionblock = _type.getFunctionblock();
      Status _status = _functionblock.getStatus();
      boolean _notEquals = (!Objects.equal(_status, null));
      if (_notEquals) {
        FunctionblockModel _type_1 = fbp.getType();
        FunctionBlock _functionblock_1 = _type_1.getFunctionblock();
        Status _status_1 = _functionblock_1.getStatus();
        EList<Property> _properties = _status_1.getProperties();
        for (final Property content : _properties) {
          {
            PropertyType _type_2 = content.getType();
            if ((_type_2 instanceof PrimitivePropertyType)) {
              String _name_1 = content.getName();
              attributeList.add(_name_1);
            }
            PropertyType _type_3 = content.getType();
            if ((_type_3 instanceof ObjectPropertyType)) {
              PropertyType _type_4 = content.getType();
              ObjectPropertyType objectType = ((ObjectPropertyType) _type_4);
              Type _type_5 = objectType.getType();
              if ((_type_5 instanceof org.eclipse.vorto.core.api.model.datatype.Enum)) {
                String _name_2 = content.getName();
                eattributeNameList.add(_name_2);
                Type _type_6 = objectType.getType();
                eattributeList.add(_type_6);
              } else {
                String _name_3 = content.getName();
                oattributeNameList.add(_name_3);
                Type _type_7 = objectType.getType();
                oattributeList.add(_type_7);
                String _eresult = eresult;
                Type _type_8 = objectType.getType();
                String _name_4 = content.getName();
                String _fillEntity = this.fillEntity(((Entity) _type_8), _name_4, hierarchy, eindex, index);
                eresult = (_eresult + _fillEntity);
                eindex++;
              }
            }
          }
        }
      }
      FunctionblockModel _type_2 = fbp.getType();
      FunctionBlock _functionblock_2 = _type_2.getFunctionblock();
      Fault _fault = _functionblock_2.getFault();
      boolean _notEquals_1 = (!Objects.equal(_fault, null));
      if (_notEquals_1) {
        FunctionblockModel _type_3 = fbp.getType();
        FunctionBlock _functionblock_3 = _type_3.getFunctionblock();
        Fault _fault_1 = _functionblock_3.getFault();
        EList<Property> _properties_1 = _fault_1.getProperties();
        for (final Property content_1 : _properties_1) {
          {
            PropertyType _type_4 = content_1.getType();
            if ((_type_4 instanceof PrimitivePropertyType)) {
              String _name_1 = content_1.getName();
              attributeList.add(_name_1);
            }
            PropertyType _type_5 = content_1.getType();
            if ((_type_5 instanceof ObjectPropertyType)) {
              PropertyType _type_6 = content_1.getType();
              ObjectPropertyType objectType = ((ObjectPropertyType) _type_6);
              Type _type_7 = objectType.getType();
              if ((_type_7 instanceof org.eclipse.vorto.core.api.model.datatype.Enum)) {
                String _name_2 = content_1.getName();
                eattributeNameList.add(_name_2);
                Type _type_8 = objectType.getType();
                eattributeList.add(_type_8);
              } else {
                String _name_3 = content_1.getName();
                oattributeNameList.add(_name_3);
                Type _type_9 = objectType.getType();
                oattributeList.add(_type_9);
                String _eresult = eresult;
                Type _type_10 = objectType.getType();
                String _name_4 = content_1.getName();
                String _fillEntity = this.fillEntity(((Entity) _type_10), _name_4, hierarchy, eindex, index);
                eresult = (_eresult + _fillEntity);
                eindex++;
              }
            }
          }
        }
      }
      FunctionblockModel _type_4 = fbp.getType();
      FunctionBlock _functionblock_4 = _type_4.getFunctionblock();
      Configuration _configuration = _functionblock_4.getConfiguration();
      boolean _notEquals_2 = (!Objects.equal(_configuration, null));
      if (_notEquals_2) {
        FunctionblockModel _type_5 = fbp.getType();
        FunctionBlock _functionblock_5 = _type_5.getFunctionblock();
        Configuration _configuration_1 = _functionblock_5.getConfiguration();
        EList<Property> _properties_2 = _configuration_1.getProperties();
        for (final Property content_2 : _properties_2) {
          {
            PropertyType _type_6 = content_2.getType();
            if ((_type_6 instanceof PrimitivePropertyType)) {
              String _name_1 = content_2.getName();
              attributeList.add(_name_1);
            }
            PropertyType _type_7 = content_2.getType();
            if ((_type_7 instanceof ObjectPropertyType)) {
              PropertyType _type_8 = content_2.getType();
              ObjectPropertyType objectType = ((ObjectPropertyType) _type_8);
              Type _type_9 = objectType.getType();
              if ((_type_9 instanceof org.eclipse.vorto.core.api.model.datatype.Enum)) {
                String _name_2 = content_2.getName();
                eattributeNameList.add(_name_2);
                Type _type_10 = objectType.getType();
                eattributeList.add(_type_10);
              } else {
                String _name_3 = content_2.getName();
                oattributeNameList.add(_name_3);
                Type _type_11 = objectType.getType();
                oattributeList.add(_type_11);
                String _eresult = eresult;
                Type _type_12 = objectType.getType();
                String _name_4 = content_2.getName();
                String _fillEntity = this.fillEntity(((Entity) _type_12), _name_4, hierarchy, eindex, index);
                eresult = (_eresult + _fillEntity);
                eindex++;
              }
            }
          }
        }
      }
      FunctionblockModel _type_6 = fbp.getType();
      FunctionBlock _functionblock_6 = _type_6.getFunctionblock();
      EList<Event> _events = _functionblock_6.getEvents();
      boolean _notEquals_3 = (!Objects.equal(_events, null));
      if (_notEquals_3) {
        FunctionblockModel _type_7 = fbp.getType();
        FunctionBlock _functionblock_7 = _type_7.getFunctionblock();
        EList<Event> _events_1 = _functionblock_7.getEvents();
        for (final Event content_3 : _events_1) {
          {
            if ((content_3 instanceof PrimitivePropertyType)) {
              String _name_1 = content_3.getName();
              attributeList.add(_name_1);
            }
            if ((content_3 instanceof ObjectPropertyType)) {
              Type _type_8 = ((ObjectPropertyType)content_3).getType();
              ObjectPropertyType objectType = ((ObjectPropertyType) _type_8);
              Type _type_9 = objectType.getType();
              if ((_type_9 instanceof org.eclipse.vorto.core.api.model.datatype.Enum)) {
                String _name_2 = content_3.getName();
                eattributeNameList.add(_name_2);
                Type _type_10 = objectType.getType();
                eattributeList.add(_type_10);
              } else {
                String _name_3 = content_3.getName();
                oattributeNameList.add(_name_3);
                Type _type_11 = objectType.getType();
                oattributeList.add(_type_11);
                String _eresult = eresult;
                Type _type_12 = objectType.getType();
                String _name_4 = content_3.getName();
                String _fillEntity = this.fillEntity(((Entity) _type_12), _name_4, hierarchy, eindex, index);
                eresult = (_eresult + _fillEntity);
                eindex++;
              }
            }
          }
        }
      }
      boolean _or = false;
      boolean _or_1 = false;
      boolean _isEmpty = attributeList.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _or_1 = true;
      } else {
        boolean _isEmpty_1 = oattributeList.isEmpty();
        boolean _not_1 = (!_isEmpty_1);
        _or_1 = _not_1;
      }
      if (_or_1) {
        _or = true;
      } else {
        boolean _isEmpty_2 = eattributeList.isEmpty();
        boolean _not_2 = (!_isEmpty_2);
        _or = _not_2;
      }
      if (_or) {
        String _fbresult = fbresult;
        fbresult = (_fbresult + (("\r\n\t\t\tString FB" + Integer.valueOf(index)) + "= \"\";"));
        String _fbresult_1 = fbresult;
        String _name_1 = fbp.getName();
        String _plus = ((("\r\n\t\t\tFB" + Integer.valueOf(index)) + " += \"INSERT INTO ") + _name_1);
        String _plus_1 = (_plus + " (");
        fbresult = (_fbresult_1 + _plus_1);
        for (final String attribute : attributeList) {
          String _fbresult_2 = fbresult;
          fbresult = (_fbresult_2 + (attribute + ","));
        }
        for (final String attribute_1 : eattributeNameList) {
          String _fbresult_3 = fbresult;
          fbresult = (_fbresult_3 + (attribute_1 + ","));
        }
        for (final String attribute_2 : oattributeNameList) {
          String _fbresult_4 = fbresult;
          fbresult = (_fbresult_4 + (attribute_2 + ","));
        }
        int _length = fbresult.length();
        int _minus = (_length - 1);
        String _substring = fbresult.substring(0, _minus);
        fbresult = _substring;
        String _fbresult_5 = fbresult;
        fbresult = (_fbresult_5 + ") VALUES ( \";");
        for (final String attribute_3 : attributeList) {
          String _fbresult_6 = fbresult;
          String _name_2 = fbp.getName();
          String _plus_2 = ((("\r\n\t\t\tFB" + Integer.valueOf(index)) + " += msg.getJSONObject(\"payload\").getJSONObject(\"") + _name_2);
          String _plus_3 = (_plus_2 + "\").get(\"");
          String _plus_4 = (_plus_3 + attribute_3);
          String _plus_5 = (_plus_4 + "\") + \",\";");
          fbresult = (_fbresult_6 + _plus_5);
        }
        for (final Type attribute_4 : eattributeList) {
          String _fbresult_7 = fbresult;
          String _name_3 = attribute_4.getName();
          String _plus_6 = ((("\r\n\t\t\tFB" + Integer.valueOf(index)) + " += \"(SELECT p_key FROM ") + _name_3);
          String _plus_7 = (_plus_6 + " WHERE enums =\" + msg.getJSONObject(\"payload\").getJSONObject(\"");
          String _name_4 = fbp.getName();
          String _plus_8 = (_plus_7 + _name_4);
          String _plus_9 = (_plus_8 + "\").get(\"");
          String _name_5 = attribute_4.getName();
          String _plus_10 = (_plus_9 + _name_5);
          String _plus_11 = (_plus_10 + "\") + \"),\";");
          fbresult = (_fbresult_7 + _plus_11);
        }
        for (final Type attribute_5 : oattributeList) {
          String _fbresult_8 = fbresult;
          String _name_6 = attribute_5.getName();
          String _plus_12 = ((("\r\n\t\t\tFB" + Integer.valueOf(index)) + " += \"(SELECT MAX(p_key) FROM ") + _name_6);
          String _plus_13 = (_plus_12 + "),\";");
          fbresult = (_fbresult_8 + _plus_13);
        }
        int _length_1 = fbresult.length();
        int _minus_1 = (_length_1 - 3);
        String _substring_1 = fbresult.substring(0, _minus_1);
        fbresult = _substring_1;
        String _fbresult_9 = fbresult;
        fbresult = (_fbresult_9 + (("\";\r\n\t\t\tFB" + Integer.valueOf(index)) + " += \")\";"));
      }
      String _result = result;
      return result = (_result + (eresult + fbresult));
    }
    
    public String fillEntity(final Type entity, final String type, final ArrayList<String> hierarchy, final int index, final int fbindex) {
      Entity entityType = ((Entity) entity);
      ArrayList<String> attributeList = new ArrayList<String>();
      ArrayList<String> eattributeNameList = new ArrayList<String>();
      BasicEList<Type> eattributeList = new BasicEList<Type>();
      ArrayList<String> oattributeNameList = new ArrayList<String>();
      BasicEList<Type> oattributeList = new BasicEList<Type>();
      String result = "";
      ArrayList<String> nexthierarchy = new ArrayList<String>();
      ArrayList<String> currenthierarchy = new ArrayList<String>();
      for (final String element : hierarchy) {
        currenthierarchy.add(element);
      }
      currenthierarchy.add(type);
      for (final String element_1 : currenthierarchy) {
        nexthierarchy.add(element_1);
      }
      EList<Property> _properties = entityType.getProperties();
      for (final Property attribute : _properties) {
        PropertyType _type = attribute.getType();
        if ((_type instanceof ObjectPropertyType)) {
          PropertyType _type_1 = attribute.getType();
          ObjectPropertyType objectType = ((ObjectPropertyType) _type_1);
          Type _type_2 = objectType.getType();
          if ((_type_2 instanceof Entity)) {
            String _name = attribute.getName();
            oattributeNameList.add(_name);
            Type _type_3 = objectType.getType();
            oattributeList.add(_type_3);
            String _result = result;
            Type _type_4 = objectType.getType();
            String _name_1 = attribute.getName();
            Object _fillEntity = this.fillEntity(((Entity) _type_4), _name_1, nexthierarchy, (index + 1), fbindex);
            result = (_result + _fillEntity);
          }
          Type _type_5 = objectType.getType();
          if ((_type_5 instanceof org.eclipse.vorto.core.api.model.datatype.Enum)) {
            String _name_2 = attribute.getName();
            eattributeNameList.add(_name_2);
            Type _type_6 = objectType.getType();
            eattributeList.add(_type_6);
          }
        } else {
          String _name_3 = attribute.getName();
          attributeList.add(_name_3);
        }
      }
      String _result_1 = result;
      String _name_4 = entity.getName();
      String _plus = (((((((("\r\n\t\t\tString EN" + Integer.valueOf(fbindex)) + Integer.valueOf(index)) + "= \"\";") + "\r\n\t\t\tEN") + Integer.valueOf(fbindex)) + Integer.valueOf(index)) + " += \"INSERT INTO ") + _name_4);
      String _plus_1 = (_plus + " (");
      result = (_result_1 + _plus_1);
      for (final String attribute_1 : attributeList) {
        String _result_2 = result;
        result = (_result_2 + (attribute_1 + ","));
      }
      for (final String attribute_2 : eattributeNameList) {
        String _result_3 = result;
        result = (_result_3 + (attribute_2 + ","));
      }
      for (final String attribute_3 : oattributeNameList) {
        String _result_4 = result;
        result = (_result_4 + (attribute_3 + ","));
      }
      int _length = result.length();
      int _minus = (_length - 1);
      String _substring = result.substring(0, _minus);
      result = _substring;
      String _result_5 = result;
      result = (_result_5 + ") VALUES ( \";");
      for (final String attribute_4 : attributeList) {
        {
          String _result_6 = result;
          result = (_result_6 + ((("\t\t\r\n\t\t\tEN" + Integer.valueOf(fbindex)) + Integer.valueOf(index)) + " += msg.getJSONObject(\"payload\")."));
          for (final String element_2 : currenthierarchy) {
            String _result_7 = result;
            result = (_result_7 + (("getJSONObject(\"" + element_2) + "\")."));
          }
          String _result_8 = result;
          result = (_result_8 + (("get(\"" + attribute_4) + "\") + \",\";"));
        }
      }
      for (final Type attribute_5 : eattributeList) {
        {
          String _result_6 = result;
          String _name_5 = attribute_5.getName();
          String _plus_2 = (((("\t\t\r\n\t\t\tEN" + Integer.valueOf(fbindex)) + Integer.valueOf(index)) + " += \"(SELECT p_key FROM ") + _name_5);
          String _plus_3 = (_plus_2 + " WHERE enums =\" + msg.getJSONObject(\"payload\")");
          result = (_result_6 + _plus_3);
          for (final String element_2 : currenthierarchy) {
            String _result_7 = result;
            result = (_result_7 + (("getJSONObject(\"" + element_2) + "\")."));
          }
          String _result_8 = result;
          String _name_6 = attribute_5.getName();
          String _plus_4 = ("get(\"" + _name_6);
          String _plus_5 = (_plus_4 + "\") + \" ),\";");
          result = (_result_8 + _plus_5);
        }
      }
      for (final Type attribute_6 : oattributeList) {
        String _result_6 = result;
        String _name_5 = attribute_6.getName();
        String _plus_2 = (((("\r\n\t\t\tEN" + Integer.valueOf(fbindex)) + Integer.valueOf(index)) + " += \"(SELECT MAX(p_key) FROM ") + _name_5);
        String _plus_3 = (_plus_2 + "),\";");
        result = (_result_6 + _plus_3);
      }
      int _length_1 = result.length();
      int _minus_1 = (_length_1 - 3);
      String _substring_1 = result.substring(0, _minus_1);
      result = _substring_1;
      String _result_7 = result;
      result = (_result_7 + ((("\";\r\n\t\t\tEN" + Integer.valueOf(fbindex)) + Integer.valueOf(index)) + " += \")\";"));
      String _result_8 = result;
      result = (_result_8 + ((("\r\n\t\t\tstmt.executeUpdate(EN" + Integer.valueOf(fbindex)) + Integer.valueOf(index)) + ");\r\n"));
      return result;
    }
    
    public String fillInformationModel(final InformationModel context, final ArrayList<String> fbplist) {
      String result = "";
      String _result = result;
      String _name = context.getName();
      String _plus = ("\r\n\t\t\tIM += \"INSERT INTO " + _name);
      String _plus_1 = (_plus + " (");
      result = (_result + _plus_1);
      for (final String fb : fbplist) {
        String _result_1 = result;
        result = (_result_1 + (fb + ","));
      }
      String _result_2 = result;
      result = (_result_2 + "measure_time) VALUES ( \";");
      for (final String fb_1 : fbplist) {
        String _result_3 = result;
        result = (_result_3 + (("\r\n\t\t\tIM += \"(SELECT MAX(p_key) FROM " + fb_1) + "),\";"));
      }
      String _result_4 = result;
      result = (_result_4 + "\r\n\t\t\tIM += msg.getJSONObject(\"payload\").get(\"measure_time\");");
      String _result_5 = result;
      result = (_result_5 + "\r\n\t\t\tIM += \")\";");
      return result;
    }
  }
  
  @Override
  public IGenerationResult generate(final InformationModel infomodel, final IMappingContext mappingContext) {
    String _serviceKey = this.getServiceKey();
    GenerationResultZip output = new GenerationResultZip(infomodel, _serviceKey);
    ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<InformationModel>();
    MyFirstGenerator.SQLTemplate _sQLTemplate = new MyFirstGenerator.SQLTemplate();
    GeneratorTaskFromFileTemplate<InformationModel> _generatorTaskFromFileTemplate = new GeneratorTaskFromFileTemplate<InformationModel>(_sQLTemplate);
    generator.addTask(_generatorTaskFromFileTemplate);
    MyFirstGenerator.JavaTemplate _javaTemplate = new MyFirstGenerator.JavaTemplate();
    GeneratorTaskFromFileTemplate<InformationModel> _generatorTaskFromFileTemplate_1 = new GeneratorTaskFromFileTemplate<InformationModel>(_javaTemplate);
    generator.addTask(_generatorTaskFromFileTemplate_1);
    generator.generate(infomodel, mappingContext, output);
    return output;
  }
  
  @Override
  public String getServiceKey() {
    return "myVortoGenerator";
  }
}
