package org.talend.designer.codegen.translators.databases.db2;

import org.talend.core.model.metadata.IMetadataColumn;

import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.MappingTypeRetriever;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.process.IConnectionCategory;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

public class TDB2OutputMainJava {
  protected static String nl;
  public static synchronized TDB2OutputMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TDB2OutputMainJava result = new TDB2OutputMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = "       ";
  protected final String TEXT_3 = NL + "            ";
  protected final String TEXT_4 = " = null;            ";
  protected final String TEXT_5 = NL + "\t\tquery_";
  protected final String TEXT_6 = " = new StringBuffer(\"\");";
  protected final String TEXT_7 = NL + "\t\tif(nb_line_";
  protected final String TEXT_8 = "==0) {" + NL + "\t";
  protected final String TEXT_9 = "            " + NL + "            java.sql.Statement stmtDrop_";
  protected final String TEXT_10 = " = conn_";
  protected final String TEXT_11 = ".createStatement();" + NL + "            stmtDrop_";
  protected final String TEXT_12 = ".execute(\"";
  protected final String TEXT_13 = "\");" + NL + "            stmtDrop_";
  protected final String TEXT_14 = ".close();" + NL + "            java.sql.Statement stmtCreate_";
  protected final String TEXT_15 = " = conn_";
  protected final String TEXT_16 = ".createStatement();";
  protected final String TEXT_17 = NL + "\t\t\t\t\t\tstmtCreate_";
  protected final String TEXT_18 = ".execute(\"";
  protected final String TEXT_19 = "\"+DynamicUtils.getCreateTableSQL(";
  protected final String TEXT_20 = ".";
  protected final String TEXT_21 = ", \"";
  protected final String TEXT_22 = "\")+\")";
  protected final String TEXT_23 = "\");";
  protected final String TEXT_24 = "\t\t\t\t" + NL + "\t\t\t\t\tstmtCreate_";
  protected final String TEXT_25 = ".execute(\"";
  protected final String TEXT_26 = ", \"+DynamicUtils.getCreateTableSQL(";
  protected final String TEXT_27 = ".";
  protected final String TEXT_28 = ", \"";
  protected final String TEXT_29 = "\")+\")";
  protected final String TEXT_30 = "\");";
  protected final String TEXT_31 = NL + "\t\t\t\tstmtCreate_";
  protected final String TEXT_32 = ".execute(\"";
  protected final String TEXT_33 = ")\");";
  protected final String TEXT_34 = NL + "\t\t\tstmtCreate_";
  protected final String TEXT_35 = ".close();";
  protected final String TEXT_36 = NL + "            java.sql.Statement stmtCreate_";
  protected final String TEXT_37 = " = conn_";
  protected final String TEXT_38 = ".createStatement();";
  protected final String TEXT_39 = NL + "\t\t\t\t\t\tstmtCreate_";
  protected final String TEXT_40 = ".execute(\"";
  protected final String TEXT_41 = "\"+DynamicUtils.getCreateTableSQL(";
  protected final String TEXT_42 = ".";
  protected final String TEXT_43 = ", \"";
  protected final String TEXT_44 = "\")+\")";
  protected final String TEXT_45 = "\");";
  protected final String TEXT_46 = "\t\t\t\t" + NL + "\t\t\t\t\t\tstmtCreate_";
  protected final String TEXT_47 = ".execute(\"";
  protected final String TEXT_48 = ", \"+DynamicUtils.getCreateTableSQL(";
  protected final String TEXT_49 = ".";
  protected final String TEXT_50 = ", \"";
  protected final String TEXT_51 = "\")+\")";
  protected final String TEXT_52 = "\");";
  protected final String TEXT_53 = NL + "\t\t\t\tstmtCreate_";
  protected final String TEXT_54 = ".execute(\"";
  protected final String TEXT_55 = ")\");";
  protected final String TEXT_56 = NL + "            stmtCreate_";
  protected final String TEXT_57 = ".close();";
  protected final String TEXT_58 = "\t         " + NL + "\t\t    " + NL + "\t\t    ";
  protected final String TEXT_59 = NL + "\t\t    String keyCheckTable_";
  protected final String TEXT_60 = " = \"[Informix]\"+\"[\"+";
  protected final String TEXT_61 = "+\"][\"+";
  protected final String TEXT_62 = "+\"][\"+";
  protected final String TEXT_63 = "+\"][\"+";
  protected final String TEXT_64 = "+\"][\"+";
  protected final String TEXT_65 = "+\"]\"+ \"[checktable]\" + \"[\" + ";
  protected final String TEXT_66 = " + \"]\";" + NL + "\t\t    ";
  protected final String TEXT_67 = NL + "\t\t    // [%connection%][checktable][tableName]" + NL + "\t\t    String keyCheckTable_";
  protected final String TEXT_68 = " = conn_";
  protected final String TEXT_69 = " + \"[checktable]\" + \"[\" + ";
  protected final String TEXT_70 = " + \"]\";" + NL + "\t\t    ";
  protected final String TEXT_71 = NL + "\t\t    " + NL + "\t\t\tif(GlobalResource.resourceMap.get(keyCheckTable_";
  protected final String TEXT_72 = ")== null){" + NL + "\t\t\t" + NL + "\t\t\t\tsynchronized (GlobalResource.resourceLockMap.get(keyCheckTable_";
  protected final String TEXT_73 = ")) {" + NL + "\t\t\t\t\tif(GlobalResource.resourceMap.get(keyCheckTable_";
  protected final String TEXT_74 = ")== null){";
  protected final String TEXT_75 = "              ";
  protected final String TEXT_76 = NL + "\t\t\t\tjava.sql.Statement isExistStmt_";
  protected final String TEXT_77 = " = conn_";
  protected final String TEXT_78 = ".createStatement();" + NL + "\t\t\t\tboolean whetherExist_";
  protected final String TEXT_79 = " = false;" + NL + "\t\t\t\ttry {" + NL + "\t\t\t\t\tisExistStmt_";
  protected final String TEXT_80 = ".execute(\"SELECT TOP 1 1 FROM [\" +  tableName_";
  protected final String TEXT_81 = " + \"]\" );" + NL + "\t\t\t\t\twhetherExist_";
  protected final String TEXT_82 = " = true;" + NL + "\t\t\t\t} catch (Exception e){" + NL + "\t\t\t\t\twhetherExist_";
  protected final String TEXT_83 = " = false;" + NL + "\t\t\t\t}" + NL + "\t\t\t\tisExistStmt_";
  protected final String TEXT_84 = ".close();";
  protected final String TEXT_85 = "   " + NL + "\t\t\t\t";
  protected final String TEXT_86 = NL + "\t\t\t\tjava.sql.DatabaseMetaData dbMetaData_";
  protected final String TEXT_87 = " = conn_";
  protected final String TEXT_88 = ".getMetaData();" + NL + "\t            java.sql.ResultSet rsTable_";
  protected final String TEXT_89 = " = dbMetaData_";
  protected final String TEXT_90 = ".getTables(null, dbschemaForSearch_";
  protected final String TEXT_91 = ", tableNameForSearch_";
  protected final String TEXT_92 = ", new String[]{\"TABLE\"});" + NL + "\t            boolean whetherExist_";
  protected final String TEXT_93 = " = false;" + NL + "\t            if(rsTable_";
  protected final String TEXT_94 = ".next()) {" + NL + "\t            \twhetherExist_";
  protected final String TEXT_95 = " = true;" + NL + "\t            }" + NL + "\t            rsTable_";
  protected final String TEXT_96 = ".close();" + NL + "\t            ";
  protected final String TEXT_97 = "  \t            " + NL + "\t\t\t\tjava.sql.DatabaseMetaData dbMetaData_";
  protected final String TEXT_98 = " = conn_";
  protected final String TEXT_99 = ".getMetaData();" + NL + "\t            java.sql.ResultSet rsTable_";
  protected final String TEXT_100 = " = dbMetaData_";
  protected final String TEXT_101 = ".getTables(null, null, null, new String[]{\"TABLE\"});" + NL + "\t            boolean whetherExist_";
  protected final String TEXT_102 = " = false;" + NL + "\t            while(rsTable_";
  protected final String TEXT_103 = ".next()) {" + NL + "\t                String table_";
  protected final String TEXT_104 = " = rsTable_";
  protected final String TEXT_105 = ".getString(\"TABLE_NAME\");" + NL + "\t                String schema_";
  protected final String TEXT_106 = " = rsTable_";
  protected final String TEXT_107 = ".getString(\"TABLE_SCHEM\");" + NL + "\t                if(table_";
  protected final String TEXT_108 = ".equalsIgnoreCase(";
  protected final String TEXT_109 = ") " + NL + "\t                \t&& (schema_";
  protected final String TEXT_110 = ".equalsIgnoreCase(dbschema_";
  protected final String TEXT_111 = ") || dbschema_";
  protected final String TEXT_112 = ".trim().length() ==0)) {" + NL + "\t                    whetherExist_";
  protected final String TEXT_113 = " = true;" + NL + "\t                    break;" + NL + "\t                }" + NL + "\t            }" + NL + "\t            rsTable_";
  protected final String TEXT_114 = ".close();";
  protected final String TEXT_115 = "      " + NL + "\t            java.sql.DatabaseMetaData dbMetaData_";
  protected final String TEXT_116 = " = conn_";
  protected final String TEXT_117 = ".getMetaData();" + NL + "\t            java.sql.ResultSet rsTable_";
  protected final String TEXT_118 = " = dbMetaData_";
  protected final String TEXT_119 = ".getTables(null, null, null, new String[]{\"TABLE\"});" + NL + "\t            boolean whetherExist_";
  protected final String TEXT_120 = " = false;" + NL + "\t            while(rsTable_";
  protected final String TEXT_121 = ".next()) {" + NL + "\t                String table_";
  protected final String TEXT_122 = " = rsTable_";
  protected final String TEXT_123 = ".getString(\"TABLE_NAME\");" + NL + "\t                if(table_";
  protected final String TEXT_124 = ".equalsIgnoreCase(";
  protected final String TEXT_125 = ")) {" + NL + "\t                    whetherExist_";
  protected final String TEXT_126 = " = true;" + NL + "\t                    break;" + NL + "\t                }" + NL + "\t            }" + NL + "\t            rsTable_";
  protected final String TEXT_127 = ".close();";
  protected final String TEXT_128 = NL + "                if(!whetherExist_";
  protected final String TEXT_129 = ") {" + NL + "                    java.sql.Statement stmtCreate_";
  protected final String TEXT_130 = " = conn_";
  protected final String TEXT_131 = ".createStatement();";
  protected final String TEXT_132 = NL + "\t\t\t\t\t\tstmtCreate_";
  protected final String TEXT_133 = ".execute(\"";
  protected final String TEXT_134 = "\"+DynamicUtils.getCreateTableSQL(";
  protected final String TEXT_135 = ".";
  protected final String TEXT_136 = ", \"";
  protected final String TEXT_137 = "\")+\")";
  protected final String TEXT_138 = "\");";
  protected final String TEXT_139 = "\t\t\t\t" + NL + "\t\t\t\t\t\tstmtCreate_";
  protected final String TEXT_140 = ".execute(\"";
  protected final String TEXT_141 = ", \"+DynamicUtils.getCreateTableSQL(";
  protected final String TEXT_142 = ".";
  protected final String TEXT_143 = ", \"";
  protected final String TEXT_144 = "\")+\")";
  protected final String TEXT_145 = "\");";
  protected final String TEXT_146 = NL + "\t\t\t\tstmtCreate_";
  protected final String TEXT_147 = ".execute(\"";
  protected final String TEXT_148 = ")\");";
  protected final String TEXT_149 = NL + "                    stmtCreate_";
  protected final String TEXT_150 = ".close();            " + NL + "                }                ";
  protected final String TEXT_151 = NL + "                if(whetherExist_";
  protected final String TEXT_152 = ") {" + NL + "                    java.sql.Statement stmtDrop_";
  protected final String TEXT_153 = " = conn_";
  protected final String TEXT_154 = ".createStatement();" + NL + "                    stmtDrop_";
  protected final String TEXT_155 = ".execute(\"";
  protected final String TEXT_156 = "\");" + NL + "                    stmtDrop_";
  protected final String TEXT_157 = ".close();" + NL + "                }" + NL + "                java.sql.Statement stmtCreate_";
  protected final String TEXT_158 = " = conn_";
  protected final String TEXT_159 = ".createStatement();";
  protected final String TEXT_160 = NL + "\t\t\t\t\t\tstmtCreate_";
  protected final String TEXT_161 = ".execute(\"";
  protected final String TEXT_162 = "\"+DynamicUtils.getCreateTableSQL(";
  protected final String TEXT_163 = ".";
  protected final String TEXT_164 = ", \"";
  protected final String TEXT_165 = "\")+\")";
  protected final String TEXT_166 = "\");";
  protected final String TEXT_167 = "\t\t\t\t" + NL + "\t\t\t\t\t\tstmtCreate_";
  protected final String TEXT_168 = ".execute(\"";
  protected final String TEXT_169 = ", \"+DynamicUtils.getCreateTableSQL(";
  protected final String TEXT_170 = ".";
  protected final String TEXT_171 = ", \"";
  protected final String TEXT_172 = "\")+\")";
  protected final String TEXT_173 = "\");";
  protected final String TEXT_174 = NL + "\t\t\t\tstmtCreate_";
  protected final String TEXT_175 = ".execute(\"";
  protected final String TEXT_176 = ")\");";
  protected final String TEXT_177 = NL + "                stmtCreate_";
  protected final String TEXT_178 = ".close();";
  protected final String TEXT_179 = NL + "\t\t\t\tGlobalResource.resourceMap.put(keyCheckTable_";
  protected final String TEXT_180 = ", true);" + NL + "" + NL + "\t\t\t\t\t} // end of if" + NL + "\t\t\t\t} // end synchronized" + NL + "\t\t\t}";
  protected final String TEXT_181 = "            ";
  protected final String TEXT_182 = NL + "            java.sql.Statement stmtClear_";
  protected final String TEXT_183 = " = conn_";
  protected final String TEXT_184 = ".createStatement();" + NL + "            stmtClear_";
  protected final String TEXT_185 = ".executeUpdate(\"";
  protected final String TEXT_186 = "\");" + NL + "            stmtClear_";
  protected final String TEXT_187 = ".close();";
  protected final String TEXT_188 = NL + "            java.sql.Statement stmtTruncCount_";
  protected final String TEXT_189 = " = conn_";
  protected final String TEXT_190 = ".createStatement();" + NL + "            java.sql.ResultSet rsTruncCount_";
  protected final String TEXT_191 = " = stmtTruncCount_";
  protected final String TEXT_192 = ".executeQuery(\"";
  protected final String TEXT_193 = "\");" + NL + "            java.sql.Statement stmtTrunc_";
  protected final String TEXT_194 = " = conn_";
  protected final String TEXT_195 = ".createStatement();" + NL + "            stmtTrunc_";
  protected final String TEXT_196 = ".executeUpdate(\"";
  protected final String TEXT_197 = "\");" + NL + "            while(rsTruncCount_";
  protected final String TEXT_198 = ".next()) {" + NL + "                deletedCount_";
  protected final String TEXT_199 = " += rsTruncCount_";
  protected final String TEXT_200 = ".getInt(1);" + NL + "            }" + NL + "            rsTruncCount_";
  protected final String TEXT_201 = ".close();" + NL + "            stmtTruncCount_";
  protected final String TEXT_202 = ".close();" + NL + "            stmtTrunc_";
  protected final String TEXT_203 = ".close();            ";
  protected final String TEXT_204 = NL + "\t\t \tjava.sql.Statement stmtTruncCount_";
  protected final String TEXT_205 = " = conn_";
  protected final String TEXT_206 = ".createStatement();" + NL + "\t\t \tjava.sql.ResultSet rsTruncCount_";
  protected final String TEXT_207 = " = stmtTruncCount_";
  protected final String TEXT_208 = ".executeQuery(\"";
  protected final String TEXT_209 = "\");" + NL + "            java.sql.Statement stmtTrunc_";
  protected final String TEXT_210 = " = conn_";
  protected final String TEXT_211 = ".createStatement();" + NL + "            stmtTrunc_";
  protected final String TEXT_212 = ".executeUpdate(\"";
  protected final String TEXT_213 = "\");" + NL + "             while(rsTruncCount_";
  protected final String TEXT_214 = ".next()) {" + NL + "                deletedCount_";
  protected final String TEXT_215 = " += rsTruncCount_";
  protected final String TEXT_216 = ".getInt(1);" + NL + "            }" + NL + "            rsTruncCount_";
  protected final String TEXT_217 = ".close();" + NL + "            stmtTruncCount_";
  protected final String TEXT_218 = ".close();" + NL + "            stmtTrunc_";
  protected final String TEXT_219 = ".close();";
  protected final String TEXT_220 = NL + "\t\t\t\tString insert_";
  protected final String TEXT_221 = " = \"INSERT INTO \\\"\" + tableName_";
  protected final String TEXT_222 = " + \"\\\" (";
  protected final String TEXT_223 = ", \"+DynamicUtils.getInsertIntoStmtColumnsList(";
  protected final String TEXT_224 = ".";
  protected final String TEXT_225 = ", \"";
  protected final String TEXT_226 = "\")+\") VALUES (";
  protected final String TEXT_227 = ", \"+DynamicUtils.getInsertIntoStmtValuesList(";
  protected final String TEXT_228 = ".";
  protected final String TEXT_229 = ")+\")\";";
  protected final String TEXT_230 = NL + "\t\t\t\tString insert_";
  protected final String TEXT_231 = " = \"INSERT INTO \\\"\" + tableName_";
  protected final String TEXT_232 = " + \"\\\" (\"+DynamicUtils.getInsertIntoStmtColumnsList(";
  protected final String TEXT_233 = ".";
  protected final String TEXT_234 = ", \"";
  protected final String TEXT_235 = "\")+\") VALUES (\"+DynamicUtils.getInsertIntoStmtValuesList(";
  protected final String TEXT_236 = ".";
  protected final String TEXT_237 = ")+\")\";";
  protected final String TEXT_238 = NL + "\t\tpstmt_";
  protected final String TEXT_239 = " = conn_";
  protected final String TEXT_240 = ".prepareStatement(insert_";
  protected final String TEXT_241 = ");" + NL + "\t    ";
  protected final String TEXT_242 = NL + "\t\t\t\tString update_";
  protected final String TEXT_243 = " = \"UPDATE \\\"\" + tableName_";
  protected final String TEXT_244 = " + \"\\\" SET ";
  protected final String TEXT_245 = ", \"+DynamicUtils.getUpdateSet(";
  protected final String TEXT_246 = ".";
  protected final String TEXT_247 = ", \"";
  protected final String TEXT_248 = "\")+\" WHERE ";
  protected final String TEXT_249 = "\";";
  protected final String TEXT_250 = NL + "\t\t\t\tString update_";
  protected final String TEXT_251 = " = \"UPDATE \\\"\" + tableName_";
  protected final String TEXT_252 = " + \"\\\" SET \"+DynamicUtils.getUpdateSet(";
  protected final String TEXT_253 = ".";
  protected final String TEXT_254 = ", \"";
  protected final String TEXT_255 = "\")+\" WHERE ";
  protected final String TEXT_256 = "\";";
  protected final String TEXT_257 = NL + "\t\tpstmt_";
  protected final String TEXT_258 = " = conn_";
  protected final String TEXT_259 = ".prepareStatement(update_";
  protected final String TEXT_260 = ");" + NL + "\t    ";
  protected final String TEXT_261 = NL + "\t\t\t\tString insert_";
  protected final String TEXT_262 = " = \"INSERT INTO \\\"\" + tableName_";
  protected final String TEXT_263 = " + \"\\\" (";
  protected final String TEXT_264 = ", \"+DynamicUtils.getInsertIntoStmtColumnsList(";
  protected final String TEXT_265 = ".";
  protected final String TEXT_266 = ", \"";
  protected final String TEXT_267 = "\")+\") VALUES (";
  protected final String TEXT_268 = ", \"+DynamicUtils.getInsertIntoStmtValuesList(";
  protected final String TEXT_269 = ".";
  protected final String TEXT_270 = ")+\")\";";
  protected final String TEXT_271 = NL + "\t\t\t\tString insert_";
  protected final String TEXT_272 = " = \"INSERT INTO \\\"\" + tableName_";
  protected final String TEXT_273 = " + \"\\\" (\"+DynamicUtils.getInsertIntoStmtColumnsList(";
  protected final String TEXT_274 = ".";
  protected final String TEXT_275 = ", \"";
  protected final String TEXT_276 = "\")+\") VALUES (\"+DynamicUtils.getInsertIntoStmtValuesList(";
  protected final String TEXT_277 = ".";
  protected final String TEXT_278 = ")+\")\";";
  protected final String TEXT_279 = NL + "\t\t\t\tString update_";
  protected final String TEXT_280 = " = \"UPDATE \\\"\" + tableName_";
  protected final String TEXT_281 = " + \"\\\" SET ";
  protected final String TEXT_282 = ", \"+DynamicUtils.getUpdateSet(";
  protected final String TEXT_283 = ".";
  protected final String TEXT_284 = ", \"";
  protected final String TEXT_285 = "\")+\" WHERE ";
  protected final String TEXT_286 = "\";";
  protected final String TEXT_287 = NL + "\t\t\t\tString update_";
  protected final String TEXT_288 = " = \"UPDATE \\\"\" + tableName_";
  protected final String TEXT_289 = " + \"\\\" SET \"+DynamicUtils.getUpdateSet(";
  protected final String TEXT_290 = ".";
  protected final String TEXT_291 = ", \"";
  protected final String TEXT_292 = "\")+\" WHERE ";
  protected final String TEXT_293 = "\";";
  protected final String TEXT_294 = NL + "\t    pstmt_";
  protected final String TEXT_295 = " = conn_";
  protected final String TEXT_296 = ".prepareStatement(\"SELECT COUNT(1) FROM \\\"\" + tableName_";
  protected final String TEXT_297 = " + \"\\\" WHERE ";
  protected final String TEXT_298 = "\");" + NL + "\t    pstmtInsert_";
  protected final String TEXT_299 = " = conn_";
  protected final String TEXT_300 = ".prepareStatement(insert_";
  protected final String TEXT_301 = ");" + NL + "\t    pstmtUpdate_";
  protected final String TEXT_302 = " = conn_";
  protected final String TEXT_303 = ".prepareStatement(update_";
  protected final String TEXT_304 = ");" + NL + "\t    ";
  protected final String TEXT_305 = NL + "\t\t\t\tString insert_";
  protected final String TEXT_306 = " = \"INSERT INTO \\\"\" + tableName_";
  protected final String TEXT_307 = " + \"\\\" (";
  protected final String TEXT_308 = ", \"+DynamicUtils.getInsertIntoStmtColumnsList(";
  protected final String TEXT_309 = ".";
  protected final String TEXT_310 = ", \"";
  protected final String TEXT_311 = "\")+\") VALUES (";
  protected final String TEXT_312 = ", \"+DynamicUtils.getInsertIntoStmtValuesList(";
  protected final String TEXT_313 = ".";
  protected final String TEXT_314 = ")+\")\";";
  protected final String TEXT_315 = NL + "\t\t\t\tString insert_";
  protected final String TEXT_316 = " = \"INSERT INTO \\\"\" + tableName_";
  protected final String TEXT_317 = " + \"\\\" (\"+DynamicUtils.getInsertIntoStmtColumnsList(";
  protected final String TEXT_318 = ".";
  protected final String TEXT_319 = ", \"";
  protected final String TEXT_320 = "\")+\") VALUES (\"+DynamicUtils.getInsertIntoStmtValuesList(";
  protected final String TEXT_321 = ".";
  protected final String TEXT_322 = ")+\")\";";
  protected final String TEXT_323 = NL + "\t\t\t\tString update_";
  protected final String TEXT_324 = " = \"UPDATE \\\"\" + tableName_";
  protected final String TEXT_325 = " + \"\\\" SET ";
  protected final String TEXT_326 = ", \"+DynamicUtils.getUpdateSet(";
  protected final String TEXT_327 = ".";
  protected final String TEXT_328 = ", \"";
  protected final String TEXT_329 = "\")+\" WHERE ";
  protected final String TEXT_330 = "\";";
  protected final String TEXT_331 = NL + "\t\t\t\tString update_";
  protected final String TEXT_332 = " = \"UPDATE \\\"\" + tableName_";
  protected final String TEXT_333 = " + \"\\\" SET \"+DynamicUtils.getUpdateSet(";
  protected final String TEXT_334 = ".";
  protected final String TEXT_335 = ", \"";
  protected final String TEXT_336 = "\")+\" WHERE ";
  protected final String TEXT_337 = "\";";
  protected final String TEXT_338 = NL + "\t    pstmtUpdate_";
  protected final String TEXT_339 = " = conn_";
  protected final String TEXT_340 = ".prepareStatement(update_";
  protected final String TEXT_341 = ");" + NL + "\t    pstmtInsert_";
  protected final String TEXT_342 = " = conn_";
  protected final String TEXT_343 = ".prepareStatement(insert_";
  protected final String TEXT_344 = ");\t\t" + NL + "\t    ";
  protected final String TEXT_345 = NL + "\t    String delete_";
  protected final String TEXT_346 = " = \"DELETE  FROM \\\"\" + tableName_";
  protected final String TEXT_347 = " + \"\\\" WHERE ";
  protected final String TEXT_348 = "\";" + NL + "\t    pstmt_";
  protected final String TEXT_349 = " = conn_";
  protected final String TEXT_350 = ".prepareStatement(delete_";
  protected final String TEXT_351 = ");" + NL + "\t    ";
  protected final String TEXT_352 = NL + "\t\tStringBuffer query_";
  protected final String TEXT_353 = " = null;" + NL + "       \t";
  protected final String TEXT_354 = NL + "\t\t \tString[] insertSQLSplits_";
  protected final String TEXT_355 = " = insert_";
  protected final String TEXT_356 = ".split(\"\\\\?\");";
  protected final String TEXT_357 = NL + "\t\t\tupdate_";
  protected final String TEXT_358 = " += \" \";" + NL + "\t    \tString[] updateSQLSplits_";
  protected final String TEXT_359 = " = update_";
  protected final String TEXT_360 = ".split(\"\\\\?\");";
  protected final String TEXT_361 = NL + "\t\t\tupdate_";
  protected final String TEXT_362 = " += \" \";" + NL + "\t\t\tString[] updateSQLSplits_";
  protected final String TEXT_363 = " = update_";
  protected final String TEXT_364 = ".split(\"\\\\?\");" + NL + "\t\t\tString[] insertSQLSplits_";
  protected final String TEXT_365 = " = insert_";
  protected final String TEXT_366 = ".split(\"\\\\?\");";
  protected final String TEXT_367 = NL + "\t\t\tdelete_";
  protected final String TEXT_368 = " += \" \";" + NL + "\t\t   \tString[] deleteSQLSplits_";
  protected final String TEXT_369 = " = delete_";
  protected final String TEXT_370 = ".split(\"\\\\?\");";
  protected final String TEXT_371 = NL + "\t\t\treplace_";
  protected final String TEXT_372 = " += \" \";" + NL + "\t\t\tString[] replaceSQLSplits_";
  protected final String TEXT_373 = " = replace_";
  protected final String TEXT_374 = ".split(\"\\\\?\");";
  protected final String TEXT_375 = NL + "\t\t\tinsertIgnore_";
  protected final String TEXT_376 = " += \" \";" + NL + "\t\t\tString[] insertIgnoreSQLSplits_";
  protected final String TEXT_377 = " = insertIgnore_";
  protected final String TEXT_378 = ".split(\"\\\\?\");";
  protected final String TEXT_379 = NL + "\t\t \tString[] insertSQLSplits_";
  protected final String TEXT_380 = " = insert_";
  protected final String TEXT_381 = ".split(\"\\\\?\");";
  protected final String TEXT_382 = NL + "\t\t}" + NL + "\t";
  protected final String TEXT_383 = NL + "\t\t\t\tcommitEvery_";
  protected final String TEXT_384 = " = buffersSize_";
  protected final String TEXT_385 = ";";
  protected final String TEXT_386 = NL + "\t\t\t\tbatchSize_";
  protected final String TEXT_387 = " = buffersSize_";
  protected final String TEXT_388 = ";";
  protected final String TEXT_389 = NL + "        whetherReject_";
  protected final String TEXT_390 = " = false;";
  protected final String TEXT_391 = NL + "                    ";
  protected final String TEXT_392 = NL + "                        query_";
  protected final String TEXT_393 = " = ";
  protected final String TEXT_394 = ";";
  protected final String TEXT_395 = NL + "\t\t\t\t\tDynamicUtils.writeColumnsToDatabse(";
  protected final String TEXT_396 = ".";
  protected final String TEXT_397 = ", pstmt_";
  protected final String TEXT_398 = ", ";
  protected final String TEXT_399 = ", \"";
  protected final String TEXT_400 = "\");" + NL + "\t\t\t\t";
  protected final String TEXT_401 = NL + "                globalMap.put(\"";
  protected final String TEXT_402 = "_QUERY\", query_";
  protected final String TEXT_403 = ".toString().trim());";
  protected final String TEXT_404 = NL + "    \t\t";
  protected final String TEXT_405 = NL + "    \t\tpstmt_";
  protected final String TEXT_406 = ".addBatch();" + NL + "    \t\tnb_line_";
  protected final String TEXT_407 = "++;    \t\t " + NL + "\t\t\t" + NL + "    \t\t  ";
  protected final String TEXT_408 = NL + "    \t\t  batchSizeCounter_";
  protected final String TEXT_409 = "++;" + NL + "    \t\t  ";
  protected final String TEXT_410 = "    \t\t" + NL + "    \t\t";
  protected final String TEXT_411 = NL + "            try {" + NL + "                insertedCount_";
  protected final String TEXT_412 = " = insertedCount_";
  protected final String TEXT_413 = " + pstmt_";
  protected final String TEXT_414 = ".executeUpdate();" + NL + "                nb_line_";
  protected final String TEXT_415 = "++;" + NL + "            } catch(Exception e) {" + NL + "                whetherReject_";
  protected final String TEXT_416 = " = true;";
  protected final String TEXT_417 = NL + "                    throw(e);";
  protected final String TEXT_418 = NL + "                        ";
  protected final String TEXT_419 = " = new ";
  protected final String TEXT_420 = "Struct();";
  protected final String TEXT_421 = NL + "                            ";
  protected final String TEXT_422 = ".";
  protected final String TEXT_423 = " = ";
  protected final String TEXT_424 = ".";
  protected final String TEXT_425 = ";";
  protected final String TEXT_426 = NL + "                        rejectedCount_";
  protected final String TEXT_427 = " = rejectedCount_";
  protected final String TEXT_428 = " + 1;";
  protected final String TEXT_429 = NL + "                        ";
  protected final String TEXT_430 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_431 = ";";
  protected final String TEXT_432 = NL + "                        System.err.print(e.getMessage());";
  protected final String TEXT_433 = NL + "            }    ";
  protected final String TEXT_434 = NL + "                    ";
  protected final String TEXT_435 = NL + "                        query_";
  protected final String TEXT_436 = " = ";
  protected final String TEXT_437 = ";";
  protected final String TEXT_438 = NL + "\t\t\t\t" + NL + "\t\t\t\t\tint count_";
  protected final String TEXT_439 = "=DynamicUtils.writeColumnsToDatabse(";
  protected final String TEXT_440 = ".";
  protected final String TEXT_441 = ", pstmt_";
  protected final String TEXT_442 = ", ";
  protected final String TEXT_443 = ", \"";
  protected final String TEXT_444 = "\");" + NL + "\t\t\t\t";
  protected final String TEXT_445 = NL + "                    " + NL + "\t\t\t\t\t";
  protected final String TEXT_446 = NL + "                    \t";
  protected final String TEXT_447 = NL + "                        query_";
  protected final String TEXT_448 = " = ";
  protected final String TEXT_449 = ";";
  protected final String TEXT_450 = "  ";
  protected final String TEXT_451 = NL + "                    ";
  protected final String TEXT_452 = NL + "                        query_";
  protected final String TEXT_453 = " = ";
  protected final String TEXT_454 = ";";
  protected final String TEXT_455 = NL + "                globalMap.put(\"";
  protected final String TEXT_456 = "_QUERY\", query_";
  protected final String TEXT_457 = ".toString().trim());";
  protected final String TEXT_458 = NL + "    \t\t";
  protected final String TEXT_459 = NL + "    \t\tpstmt_";
  protected final String TEXT_460 = ".addBatch();" + NL + "    \t\tnb_line_";
  protected final String TEXT_461 = "++;    \t\t " + NL + "\t\t\t" + NL + "    \t\t  ";
  protected final String TEXT_462 = NL + "    \t\t  batchSizeCounter_";
  protected final String TEXT_463 = "++;" + NL + "    \t\t  ";
  protected final String TEXT_464 = "    \t\t" + NL + "    \t\t";
  protected final String TEXT_465 = NL + "            try {" + NL + "                updatedCount_";
  protected final String TEXT_466 = " = updatedCount_";
  protected final String TEXT_467 = " + pstmt_";
  protected final String TEXT_468 = ".executeUpdate();" + NL + "                nb_line_";
  protected final String TEXT_469 = "++;" + NL + "            } catch(Exception e) {" + NL + "                whetherReject_";
  protected final String TEXT_470 = " = true;";
  protected final String TEXT_471 = NL + "                    throw(e);";
  protected final String TEXT_472 = NL + "                        ";
  protected final String TEXT_473 = " = new ";
  protected final String TEXT_474 = "Struct();";
  protected final String TEXT_475 = NL + "                            ";
  protected final String TEXT_476 = ".";
  protected final String TEXT_477 = " = ";
  protected final String TEXT_478 = ".";
  protected final String TEXT_479 = ";";
  protected final String TEXT_480 = NL + "                        rejectedCount_";
  protected final String TEXT_481 = " = rejectedCount_";
  protected final String TEXT_482 = " + 1;";
  protected final String TEXT_483 = NL + "                        ";
  protected final String TEXT_484 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_485 = ";";
  protected final String TEXT_486 = NL + "                        System.err.print(e.getMessage());";
  protected final String TEXT_487 = NL + "            }";
  protected final String TEXT_488 = NL + "\t\t\t\t\t";
  protected final String TEXT_489 = NL + "                    \t";
  protected final String TEXT_490 = NL + "                    ";
  protected final String TEXT_491 = NL + "    " + NL + "    \t\tjava.sql.ResultSet rs_";
  protected final String TEXT_492 = " = pstmt_";
  protected final String TEXT_493 = ".executeQuery();" + NL + "    \t\tint checkCount_";
  protected final String TEXT_494 = " = -1;" + NL + "    \t\twhile(rs_";
  protected final String TEXT_495 = ".next()) {" + NL + "    \t\t\tcheckCount_";
  protected final String TEXT_496 = " = rs_";
  protected final String TEXT_497 = ".getInt(1);" + NL + "    \t\t}" + NL + "    \t\tif(checkCount_";
  protected final String TEXT_498 = " > 0) {" + NL + "    \t\t\t";
  protected final String TEXT_499 = NL + "                        ";
  protected final String TEXT_500 = NL + "                            query_";
  protected final String TEXT_501 = " = ";
  protected final String TEXT_502 = ";";
  protected final String TEXT_503 = NL + "\t\t\t\t\t" + NL + "\t\t\t\t\t\tint count_";
  protected final String TEXT_504 = "=DynamicUtils.writeColumnsToDatabse(";
  protected final String TEXT_505 = ".";
  protected final String TEXT_506 = ", pstmtUpdate_";
  protected final String TEXT_507 = ", ";
  protected final String TEXT_508 = ", \"";
  protected final String TEXT_509 = "\");" + NL + "\t\t\t\t\t";
  protected final String TEXT_510 = NL + "\t                    \t";
  protected final String TEXT_511 = NL + "\t                    ";
  protected final String TEXT_512 = NL + "\t                        query_";
  protected final String TEXT_513 = " = ";
  protected final String TEXT_514 = ";" + NL + "\t                        ";
  protected final String TEXT_515 = NL + "                        ";
  protected final String TEXT_516 = NL + "                            query_";
  protected final String TEXT_517 = " = ";
  protected final String TEXT_518 = ";";
  protected final String TEXT_519 = NL + "                    globalMap.put(\"";
  protected final String TEXT_520 = "_QUERY\", query_";
  protected final String TEXT_521 = ".toString().trim());";
  protected final String TEXT_522 = NL + "                try {" + NL + "                    updatedCount_";
  protected final String TEXT_523 = " = updatedCount_";
  protected final String TEXT_524 = " + pstmtUpdate_";
  protected final String TEXT_525 = ".executeUpdate(); " + NL + "                } catch(Exception e) {" + NL + "                    whetherReject_";
  protected final String TEXT_526 = " = true;";
  protected final String TEXT_527 = NL + "                        throw(e);";
  protected final String TEXT_528 = NL + "                            ";
  protected final String TEXT_529 = " = new ";
  protected final String TEXT_530 = "Struct();";
  protected final String TEXT_531 = NL + "                                ";
  protected final String TEXT_532 = ".";
  protected final String TEXT_533 = " = ";
  protected final String TEXT_534 = ".";
  protected final String TEXT_535 = ";";
  protected final String TEXT_536 = NL + "                            rejectedCount_";
  protected final String TEXT_537 = " = rejectedCount_";
  protected final String TEXT_538 = " + 1;";
  protected final String TEXT_539 = NL + "                            ";
  protected final String TEXT_540 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_541 = ";";
  protected final String TEXT_542 = NL + "                            System.err.print(e.getMessage());";
  protected final String TEXT_543 = NL + "                }" + NL + "    \t\t} else {" + NL + "    \t\t\t";
  protected final String TEXT_544 = NL + "                        ";
  protected final String TEXT_545 = NL + "                            query_";
  protected final String TEXT_546 = " = ";
  protected final String TEXT_547 = ";";
  protected final String TEXT_548 = NL + "\t\t\t\t\t\tDynamicUtils.writeColumnsToDatabse(";
  protected final String TEXT_549 = ".";
  protected final String TEXT_550 = ", pstmtInsert_";
  protected final String TEXT_551 = ", ";
  protected final String TEXT_552 = ", \"";
  protected final String TEXT_553 = "\");" + NL + "\t\t\t\t\t";
  protected final String TEXT_554 = NL + "                    globalMap.put(\"";
  protected final String TEXT_555 = "_QUERY\", query_";
  protected final String TEXT_556 = ".toString().trim());";
  protected final String TEXT_557 = NL + "                try {" + NL + "                    insertedCount_";
  protected final String TEXT_558 = " = insertedCount_";
  protected final String TEXT_559 = " + pstmtInsert_";
  protected final String TEXT_560 = ".executeUpdate();" + NL + "                } catch(Exception e) {" + NL + "                    whetherReject_";
  protected final String TEXT_561 = " = true;";
  protected final String TEXT_562 = NL + "                        throw(e);";
  protected final String TEXT_563 = NL + "                            ";
  protected final String TEXT_564 = " = new ";
  protected final String TEXT_565 = "Struct();";
  protected final String TEXT_566 = NL + "                                ";
  protected final String TEXT_567 = ".";
  protected final String TEXT_568 = " = ";
  protected final String TEXT_569 = ".";
  protected final String TEXT_570 = ";";
  protected final String TEXT_571 = NL + "                            rejectedCount_";
  protected final String TEXT_572 = " = rejectedCount_";
  protected final String TEXT_573 = " + 1;";
  protected final String TEXT_574 = NL + "                            ";
  protected final String TEXT_575 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_576 = ";";
  protected final String TEXT_577 = NL + "                            System.err.print(e.getMessage());";
  protected final String TEXT_578 = NL + "                }" + NL + "    \t\t}" + NL + "    \t\tnb_line_";
  protected final String TEXT_579 = "++;" + NL + "    \t\t";
  protected final String TEXT_580 = NL + "    \t    int updateFlag_";
  protected final String TEXT_581 = "=0;" + NL + "    \t    ";
  protected final String TEXT_582 = NL + "                    ";
  protected final String TEXT_583 = NL + "                        query_";
  protected final String TEXT_584 = " = ";
  protected final String TEXT_585 = ";";
  protected final String TEXT_586 = NL + "\t\t\t\t" + NL + "\t\t\t\t\tint count_";
  protected final String TEXT_587 = "=DynamicUtils.writeColumnsToDatabse(";
  protected final String TEXT_588 = ".";
  protected final String TEXT_589 = ", pstmtUpdate_";
  protected final String TEXT_590 = ", ";
  protected final String TEXT_591 = ", \"";
  protected final String TEXT_592 = "\");" + NL + "\t\t\t\t";
  protected final String TEXT_593 = NL + "\t\t\t\t\t";
  protected final String TEXT_594 = NL + "                    \t";
  protected final String TEXT_595 = NL + "\t                        query_";
  protected final String TEXT_596 = " = ";
  protected final String TEXT_597 = ";" + NL + "\t                        ";
  protected final String TEXT_598 = NL + "                    ";
  protected final String TEXT_599 = NL + "                    ";
  protected final String TEXT_600 = NL + "                        query_";
  protected final String TEXT_601 = " = ";
  protected final String TEXT_602 = ";";
  protected final String TEXT_603 = NL + "                globalMap.put(\"";
  protected final String TEXT_604 = "_QUERY\", query_";
  protected final String TEXT_605 = ".toString().trim());";
  protected final String TEXT_606 = NL + "    \t" + NL + "            try {" + NL + "                updateFlag_";
  protected final String TEXT_607 = "=pstmtUpdate_";
  protected final String TEXT_608 = ".executeUpdate();" + NL + "                updatedCount_";
  protected final String TEXT_609 = " = updatedCount_";
  protected final String TEXT_610 = " + updateFlag_";
  protected final String TEXT_611 = ";" + NL + "            } catch(Exception e) {" + NL + "                whetherReject_";
  protected final String TEXT_612 = " = true;";
  protected final String TEXT_613 = NL + "                    throw(e);";
  protected final String TEXT_614 = NL + "                        ";
  protected final String TEXT_615 = " = new ";
  protected final String TEXT_616 = "Struct();";
  protected final String TEXT_617 = NL + "                            ";
  protected final String TEXT_618 = ".";
  protected final String TEXT_619 = " = ";
  protected final String TEXT_620 = ".";
  protected final String TEXT_621 = ";";
  protected final String TEXT_622 = NL + "                        rejectedCount_";
  protected final String TEXT_623 = " = rejectedCount_";
  protected final String TEXT_624 = " + 1;";
  protected final String TEXT_625 = NL + "                        ";
  protected final String TEXT_626 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_627 = ";";
  protected final String TEXT_628 = NL + "                        System.err.print(e.getMessage());";
  protected final String TEXT_629 = NL + "            }" + NL + "            if(updateFlag_";
  protected final String TEXT_630 = " == 0) {" + NL + "            \t";
  protected final String TEXT_631 = NL + "\t\t\t\tquery_";
  protected final String TEXT_632 = " = new StringBuffer(\"\");" + NL + "        \t\t";
  protected final String TEXT_633 = NL + "                        ";
  protected final String TEXT_634 = NL + "                            query_";
  protected final String TEXT_635 = " = ";
  protected final String TEXT_636 = ";";
  protected final String TEXT_637 = NL + "\t\t\t\t\t\tDynamicUtils.writeColumnsToDatabse(";
  protected final String TEXT_638 = ".";
  protected final String TEXT_639 = ", pstmtInsert_";
  protected final String TEXT_640 = ", ";
  protected final String TEXT_641 = ", \"";
  protected final String TEXT_642 = "\");" + NL + "\t\t\t\t\t";
  protected final String TEXT_643 = NL + "                    globalMap.put(\"";
  protected final String TEXT_644 = "_QUERY\", query_";
  protected final String TEXT_645 = ".toString().trim());";
  protected final String TEXT_646 = NL + "                try {" + NL + "                    insertedCount_";
  protected final String TEXT_647 = " = insertedCount_";
  protected final String TEXT_648 = " + pstmtInsert_";
  protected final String TEXT_649 = ".executeUpdate();" + NL + "                } catch(Exception e) {" + NL + "                    whetherReject_";
  protected final String TEXT_650 = " = true;";
  protected final String TEXT_651 = NL + "                        throw(e);";
  protected final String TEXT_652 = NL + "                            ";
  protected final String TEXT_653 = " = new ";
  protected final String TEXT_654 = "Struct();";
  protected final String TEXT_655 = NL + "                                ";
  protected final String TEXT_656 = ".";
  protected final String TEXT_657 = " = ";
  protected final String TEXT_658 = ".";
  protected final String TEXT_659 = ";";
  protected final String TEXT_660 = NL + "                            rejectedCount_";
  protected final String TEXT_661 = " = rejectedCount_";
  protected final String TEXT_662 = " + 1;";
  protected final String TEXT_663 = NL + "                            ";
  protected final String TEXT_664 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_665 = ";";
  protected final String TEXT_666 = NL + "                            System.err.print(e.getMessage());";
  protected final String TEXT_667 = NL + "                }" + NL + "            } " + NL + "    " + NL + "            nb_line_";
  protected final String TEXT_668 = "++;\t\t";
  protected final String TEXT_669 = NL + "\t\t\t\t\t";
  protected final String TEXT_670 = NL + "                    \t";
  protected final String TEXT_671 = NL + "\t                        query_";
  protected final String TEXT_672 = " = ";
  protected final String TEXT_673 = ";" + NL + "\t                        ";
  protected final String TEXT_674 = NL + "                    ";
  protected final String TEXT_675 = NL + "                    ";
  protected final String TEXT_676 = NL + "                        query_";
  protected final String TEXT_677 = " = ";
  protected final String TEXT_678 = ";";
  protected final String TEXT_679 = NL + "                globalMap.put(\"";
  protected final String TEXT_680 = "_QUERY\", query_";
  protected final String TEXT_681 = ".toString().trim());";
  protected final String TEXT_682 = NL + "    \t\t";
  protected final String TEXT_683 = NL + "    \t\tpstmt_";
  protected final String TEXT_684 = ".addBatch();" + NL + "    \t\tnb_line_";
  protected final String TEXT_685 = "++;    \t\t " + NL + "\t\t\t" + NL + "    \t\t  ";
  protected final String TEXT_686 = NL + "    \t\t  batchSizeCounter_";
  protected final String TEXT_687 = "++;" + NL + "    \t\t  ";
  protected final String TEXT_688 = "    \t\t" + NL + "    \t\t";
  protected final String TEXT_689 = NL + "            try {" + NL + "            \tdeletedCount_";
  protected final String TEXT_690 = " = deletedCount_";
  protected final String TEXT_691 = " + pstmt_";
  protected final String TEXT_692 = ".executeUpdate();" + NL + "            } catch(Exception e) {" + NL + "                whetherReject_";
  protected final String TEXT_693 = " = true;";
  protected final String TEXT_694 = NL + "                    throw(e);";
  protected final String TEXT_695 = NL + "                        ";
  protected final String TEXT_696 = " = new ";
  protected final String TEXT_697 = "Struct();";
  protected final String TEXT_698 = NL + "                            ";
  protected final String TEXT_699 = ".";
  protected final String TEXT_700 = " = ";
  protected final String TEXT_701 = ".";
  protected final String TEXT_702 = ";";
  protected final String TEXT_703 = NL + "                        rejectedCount_";
  protected final String TEXT_704 = " = rejectedCount_";
  protected final String TEXT_705 = " + 1;";
  protected final String TEXT_706 = NL + "                        ";
  protected final String TEXT_707 = ".errorMessage = e.getMessage() + \" - Line: \" + tos_count_";
  protected final String TEXT_708 = ";";
  protected final String TEXT_709 = NL + "                        System.err.print(e.getMessage());";
  protected final String TEXT_710 = NL + "            }";
  protected final String TEXT_711 = NL + "            nb_line_";
  protected final String TEXT_712 = "++;" + NL + "    \t";
  protected final String TEXT_713 = NL + "            if(!whetherReject_";
  protected final String TEXT_714 = ") {";
  protected final String TEXT_715 = NL + "                            ";
  protected final String TEXT_716 = " = new ";
  protected final String TEXT_717 = "Struct();";
  protected final String TEXT_718 = NL + "                                ";
  protected final String TEXT_719 = ".";
  protected final String TEXT_720 = " = ";
  protected final String TEXT_721 = ".";
  protected final String TEXT_722 = ";";
  protected final String TEXT_723 = NL + "            }";
  protected final String TEXT_724 = NL + "    \t\t\tif ( batchSize_";
  protected final String TEXT_725 = " <= batchSizeCounter_";
  protected final String TEXT_726 = ") {                ";
  protected final String TEXT_727 = "                                                " + NL + "                try {" + NL + "\t\t\t\t\t\tint countSum_";
  protected final String TEXT_728 = " = 0;" + NL + "\t\t\t\t\t\tfor(int countEach_";
  protected final String TEXT_729 = ": pstmt_";
  protected final String TEXT_730 = ".executeBatch()) {" + NL + "\t\t\t\t\t\t\tcountSum_";
  protected final String TEXT_731 = " += (countEach_";
  protected final String TEXT_732 = " < 0 ? 0 : countEach_";
  protected final String TEXT_733 = ");" + NL + "\t\t\t\t\t\t}                \t" + NL + "            \t    \t";
  protected final String TEXT_734 = NL + "            \t    \t\tinsertedCount_";
  protected final String TEXT_735 = " += countSum_";
  protected final String TEXT_736 = ";" + NL + "            \t    \t";
  protected final String TEXT_737 = NL + "            \t    \t\tupdatedCount_";
  protected final String TEXT_738 = " += countSum_";
  protected final String TEXT_739 = ";" + NL + "            \t    \t";
  protected final String TEXT_740 = NL + "            \t    \t    deletedCount_";
  protected final String TEXT_741 = " += countSum_";
  protected final String TEXT_742 = ";" + NL + "            \t    \t";
  protected final String TEXT_743 = "                         \t    \t" + NL + "            \t    \tbatchSizeCounter_";
  protected final String TEXT_744 = " = 0;             \t                \t                \t" + NL + "                }catch (java.sql.BatchUpdateException e){" + NL + "                \t";
  protected final String TEXT_745 = NL + "                \t\tthrow(e);" + NL + "                \t";
  protected final String TEXT_746 = NL + "                \tint countSum_";
  protected final String TEXT_747 = " = 0;" + NL + "\t\t\t\t\tfor(int countEach_";
  protected final String TEXT_748 = ": e.getUpdateCounts()) {" + NL + "\t\t\t\t\t\tcountSum_";
  protected final String TEXT_749 = " += (countEach_";
  protected final String TEXT_750 = " < 0 ? 0 : countEach_";
  protected final String TEXT_751 = ");" + NL + "\t\t\t\t\t}" + NL + "\t\t\t\t\t";
  protected final String TEXT_752 = NL + "\t\t\t    \t\tinsertedCount_";
  protected final String TEXT_753 = " += countSum_";
  protected final String TEXT_754 = "; " + NL + "\t\t\t    \t";
  protected final String TEXT_755 = NL + "\t\t\t    \t\tupdatedCount_";
  protected final String TEXT_756 = " += countSum_";
  protected final String TEXT_757 = ";" + NL + "\t\t\t    \t";
  protected final String TEXT_758 = NL + "\t\t\t    \t    deletedCount_";
  protected final String TEXT_759 = " += countSum_";
  protected final String TEXT_760 = ";" + NL + "\t\t\t    \t";
  protected final String TEXT_761 = " " + NL + "                \tSystem.err.println(e.getMessage());" + NL + "                \t";
  protected final String TEXT_762 = NL + "                }";
  protected final String TEXT_763 = "    \t\t\t" + NL + "    \t\t\t" + NL + "    \t\t\t" + NL + "    \t\t\t" + NL + "    \t\t\t}" + NL + "    \t\t";
  protected final String TEXT_764 = NL + "    \t\t    commitCounter_";
  protected final String TEXT_765 = "++;            " + NL + "                if(commitEvery_";
  protected final String TEXT_766 = " <= commitCounter_";
  protected final String TEXT_767 = ") {" + NL + "                ";
  protected final String TEXT_768 = NL + "                try {" + NL + "                \t\tint countSum_";
  protected final String TEXT_769 = " = 0;" + NL + "\t\t\t\t\t\tfor(int countEach_";
  protected final String TEXT_770 = ": pstmt_";
  protected final String TEXT_771 = ".executeBatch()) {" + NL + "\t\t\t\t\t\t\tcountSum_";
  protected final String TEXT_772 = " += (countEach_";
  protected final String TEXT_773 = " < 0 ? 0 : countEach_";
  protected final String TEXT_774 = ");" + NL + "\t\t\t\t\t\t}" + NL + "            \t    \t";
  protected final String TEXT_775 = NL + "            \t    \t\tinsertedCount_";
  protected final String TEXT_776 = " += countSum_";
  protected final String TEXT_777 = ";" + NL + "            \t    \t";
  protected final String TEXT_778 = "            \t    \t\t" + NL + "            \t    \t\tupdatedCount_";
  protected final String TEXT_779 = " += countSum_";
  protected final String TEXT_780 = ";" + NL + "            \t    \t";
  protected final String TEXT_781 = NL + "            \t    \t    deletedCount_";
  protected final String TEXT_782 = " += countSum_";
  protected final String TEXT_783 = ";" + NL + "            \t    \t";
  protected final String TEXT_784 = "            \t    " + NL + "                }catch (java.sql.BatchUpdateException e){" + NL + "                \t";
  protected final String TEXT_785 = NL + "\t\t\t\t\t\tthrow(e);" + NL + "                \t";
  protected final String TEXT_786 = NL + "                \t\tint countSum_";
  protected final String TEXT_787 = " = 0;" + NL + "\t\t\t\t\t\tfor(int countEach_";
  protected final String TEXT_788 = ": e.getUpdateCounts()) {" + NL + "\t\t\t\t\t\t\tcountSum_";
  protected final String TEXT_789 = " += (countEach_";
  protected final String TEXT_790 = " < 0 ? 0 : countEach_";
  protected final String TEXT_791 = ");" + NL + "\t\t\t\t\t\t}" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_792 = NL + "            \t    \t\tinsertedCount_";
  protected final String TEXT_793 = " += countSum_";
  protected final String TEXT_794 = ";" + NL + "            \t    \t";
  protected final String TEXT_795 = "            \t    \t\t" + NL + "            \t    \t\tupdatedCount_";
  protected final String TEXT_796 = " += countSum_";
  protected final String TEXT_797 = ";" + NL + "            \t    \t";
  protected final String TEXT_798 = NL + "            \t    \t    deletedCount_";
  protected final String TEXT_799 = " += countSum_";
  protected final String TEXT_800 = ";" + NL + "            \t    \t";
  protected final String TEXT_801 = NL + "                        System.out.println(e.getMessage());" + NL + "                \t";
  protected final String TEXT_802 = NL + "                " + NL + "                }            \t                              ";
  protected final String TEXT_803 = NL + "                \tconn_";
  protected final String TEXT_804 = ".commit();                " + NL + "                \tcommitCounter_";
  protected final String TEXT_805 = "=0;" + NL + "                }\t";
  protected final String TEXT_806 = NL;

    public static final String INSERT_COLUMN_NAME = "insertColName";
    public static final String INSERT_VALUE_STMT = "insertValueStmt";
    public static final String UPDATE_SET_STMT = "updateSetStmt";
    public static final String UPDATE_WHERE_STMT = "updateWhereStmt";
    public static final String DELETE_WHERE_STMT = "deleteWhereStmt";
    public static final String FIRST_UPDATE_KEY = "firstUpdateKeyColumn";
    public static final String FIRST_DELETE_KEY = "firstDeleteKeyColumn";
    public static final String FIRST_INSERT_COLUMN = "firstInsertColumn";
    public static final String FIRST_UPDATE_COLUMN = "firstUpdateColumn";
    public static final int NORMAL_TYPE = 0;
    public static final int INSERT_TYPE = 1;
    public static final int UPDATE_TYPE = 2;
    public static final String ACCESS = "access_id";
    public static final String AS400 = "as400_id";
    public static final String DB2 = "ibmdb2_id";
    public static final String FIREBIRD = "firebird_id";
    public static final String HSQLDB = "hsqldb_id";
	public static final String HIVE = "hive_id";
    public static final String INFORMIX = "informix_id";
    public static final String INGRES = "ingres_id";
    public static final String VECTORWISE = "vectorwise_id";
    public static final String INTERBASE = "interbase_id";
    public static final String JAVADB = "javadb_id";
    public static final String MAXDB = "maxdb_id";
    public static final String MSSQL = "id_MSSQL";
    public static final String MYSQL = "mysql_id";
    public static final String NETEZZA = "netezza_id";
    public static final String ORACLE = "oracle_id";
    public static final String POSTGRESQL = "postgres_id";
    public static final String POSTGREPLUS = "postgresplus_id";
    public static final String SQLITE = "sqlite_id";
    public static final String SYBASE = "sybase_id";
    public static final String TERADATA = "teradata_id";
    public static final String VERTICA = "vertica_id";
    public static final String ODBC = "MSODBC";
    public static final String JDBC = "JDBC";
    private static Map<String, Manager> managerMap = new HashMap<String, Manager>();
    public class Column {
        IMetadataColumn column;
        String name;
        String dataType;
        String operator;
        String columnName;
        String sqlStmt;
        String value;
        boolean addCol;
        boolean isKey;
        boolean isAutoIncrement;
        int startValue;
        int step;
        boolean isUpdateKey;
        boolean isDeleteKey;
        boolean insertable = true;
        boolean updatable = true;
        List<Column> replacement = new ArrayList<Column>();
        public Column(String colName, String sqlStmt, boolean addCol) {
            this.column = null;
            this.name = colName;
            this.columnName = colName;
            this.sqlStmt = sqlStmt;
            this.value = "?";
            this.addCol = addCol;
        }
        public Column(IMetadataColumn column) {
            this.column = column;
            this.name = column.getLabel();
            this.sqlStmt = "?";
            this.value = "?";
            this.addCol = false;
            this.columnName = column.getOriginalDbColumnName();
        }
        public Column(IMetadataColumn column, boolean isKey, boolean useFieldOptions, Map<String, String> fieldOption, boolean isSpecifyIdentityKey, String identityKey, int startValue, int step) {
            this(column, isKey, useFieldOptions, fieldOption);
            if(isSpecifyIdentityKey) {
                if(column.getLabel().equals(identityKey)) {
                    isAutoIncrement = false;
                    this.startValue = startValue;
                    this.step = step;
                }
            }
        }
        public Column(IMetadataColumn column, boolean isKey, boolean useFieldOptions, Map<String, String> fieldOption) {
            this(column);
            this.isKey = isKey;
            if(useFieldOptions) {
                this.isUpdateKey = fieldOption.get("UPDATE_KEY").equals("true");
                this.isDeleteKey = fieldOption.get("DELETE_KEY").equals("true");
                this.insertable = fieldOption.get("INSERTABLE").equals("true");
                this.updatable = fieldOption.get("UPDATABLE").equals("true");                
            } else {
                this.insertable = true;
                if (isKey) {
                    this.isUpdateKey = true;
                    this.isDeleteKey = true;
                    this.updatable = false;
                }
                else {
                    this.isUpdateKey = false;
                    this.isDeleteKey = false;
                    this.updatable = true;
                }                
            }
        }
        public boolean isReplaced() {
            return replacement.size() > 0;
        }
        public void replace(Column column) {
            this.replacement.add(column);
        }
        public List<Column> getReplacement() {
            return this.replacement;
        }
        public void setColumn(IMetadataColumn column) {
            this.column = column;
        }
        public IMetadataColumn getColumn() {
            return this.column;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }
        public String getColumnName() {
            return this.columnName;
        }
        public void setIsAddCol(boolean isadd) {
            this.addCol = isadd;
        }
        public boolean isAddCol() {
            return this.addCol;
        }
        public void setSqlStmt(String sql) {
            this.sqlStmt = sql;
        }
        public String getSqlStmt() {
			if(this.getColumn()!=null) {
				if (this.getColumn().getTalendType().equals("id_Geometry")) {
				    
                    if ("MDSYS.SDO_GEOMETRY".equalsIgnoreCase(this.getColumn().getType())) {
                        return "?";
                    } else {
	    			    return "GeomFromText(?, ?)";    // For PostGIS
                    }
                } else {
					return this.sqlStmt;
				}
			} else {
				return this.sqlStmt;
			}
        }
        public void setValue(String value) {
            this.value = value;
        }
        public String getValue() {
            return this.value;
        }
        public void setDataType(String dataType) {
            this.dataType = dataType;
        }
        public String getDataType() {
            return dataType;
        }
        public void setOperator(String operator) {
            this.operator = operator;
        }
        public String getOperator() {
            return operator;
        }
        public boolean isAutoIncrement() {
            return this.isAutoIncrement;
        }
        public void setAutoIncrement(boolean isAutoIncrement) {
            this.isAutoIncrement = isAutoIncrement;
        }
        public int getStartValue() {
            return this.startValue;
        }
        public void setStartValue(int startValue) {
            this.startValue = startValue;
        }
        public int getStep() {
            return this.step;
        }
        public void setStep(int step) {
            this.step = step;
        }
        public boolean isKey() {
            return this.isKey;
        }
        public void setKey(boolean isKey) {
            this.isKey = isKey;
        }
        public boolean isUpdateKey() {
            return this.isUpdateKey;
        }
        public void setUpdateKey(boolean isUpdateKey) {
            this.isUpdateKey = isUpdateKey;
        }
        public boolean isDeleteKey() {
            return this.isDeleteKey;
        }
        public void setDeleteKey(boolean isDeleteKey) {
            this.isDeleteKey = isDeleteKey;
        }
        public boolean isInsertable() {
            return this.insertable;
        }
        public void setInsertable(boolean insertable) {
            this.insertable = insertable;
        }
        public boolean isUpdatable() {
            return this.updatable;
        }
        public void setUpdatable(boolean updatable) {
            this.updatable = updatable;
        }
    }
        
    public Column getColumn(IMetadataColumn column) {
        return new Column(column);
    }

    public Column getColumn(String colName, String sqlStmt, boolean addCol) {
        return new Column(colName, sqlStmt, addCol);
    }
    
    public Column getColumn(IMetadataColumn column, boolean isKey, boolean useFieldsOptions, Map<String, String> fieldOption) {
        return new Column(column, isKey, useFieldsOptions, fieldOption);
    }

    public abstract class Manager {
        
        protected INode node;// add the Node, it will be more convenient

        public void setNode(INode node) {
           this.node = node;
        }
        
        protected String cid;        
        protected abstract String getDBMSId();
        protected abstract String getLProtectedChar();
        protected abstract String getRProtectedChar();        
        public Manager() {}
        public Manager(String cid) {
            this.cid = cid;
        }
      protected  String getLProtectedChar(String columName) {
    	  return getLProtectedChar();
      }
      protected  String getRProtectedChar(String columName) {
    	  return getRProtectedChar();        
      }
        public String getSelectionSQL() {
            StringBuilder selectionSQL = new StringBuilder();
            selectionSQL.append("SELECT COUNT(1) FROM " + getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar());
            return selectionSQL.toString();
        }
        public String getDeleteTableSQL() {
            StringBuilder deleteTableSQL = new StringBuilder();
            deleteTableSQL.append("DELETE FROM " + getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar());
            return deleteTableSQL.toString();
        }
        public String getTruncateTableSQL() {
            StringBuilder truncateTableSQL = new StringBuilder();
            truncateTableSQL.append("TRUNCATE TABLE " + getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar());
            return truncateTableSQL.toString();
        }
        public String getTruncateReuseStroageTableSQL() {
            StringBuilder truncate_reuse_stroage_TableSQL = new StringBuilder();
            truncate_reuse_stroage_TableSQL.append("TRUNCATE TABLE " + getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + " REUSE STORAGE " + getRProtectedChar());
            return truncate_reuse_stroage_TableSQL.toString();
        }
        public String getDropTableSQL() {
            StringBuilder dropTableSQL = new StringBuilder();
            dropTableSQL.append("DROP TABLE " + getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar());
            return dropTableSQL.toString();
        }
        public String getCreateTableSQL(List<Column> columnList) {
            MappingTypeRetriever mappingType = MetadataTalendType.getMappingTypeRetriever(getDBMSId());
            StringBuilder createSQL = new StringBuilder();
            createSQL.append("CREATE TABLE " + getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "(");
            List<String> pkList = new ArrayList<String>();
            int count = 0;
            String ending = ",";
            for (Column column : columnList) {
                if (column.isReplaced()) {
                    List<Column> replacedColumns = column.getReplacement();
                    if (column.isKey()) {
                        for (Column replacedColumn : replacedColumns) {
                            pkList.add(getLProtectedChar(replacedColumn.getColumnName()) + replacedColumn.getColumnName() + getRProtectedChar(replacedColumn.getColumnName()));
                        }
                    }
                    int replacedCount = 0;
                    for (Column replacedColumn : replacedColumns) {
                        if (count == columnList.size() - 1 && replacedCount == replacedColumns.size() - 1 && pkList.size() == 0) {
                            ending = "";
                        }
                        createSQL.append(getLProtectedChar(replacedColumn.getColumnName()) + replacedColumn.getColumnName() + getRProtectedChar(replacedColumn.getColumnName()) + " ");
                        createSQL.append(replacedColumn.getDataType() + ending);
                        replacedCount++;
                    }
                } else {
                    if (column.isAddCol()) {
                        if (count == columnList.size() - 1 && pkList.size() == 0) {
                            ending = "";
                        }
                        createSQL.append(getLProtectedChar( column.getColumnName() ) + column.getColumnName() + getRProtectedChar( column.getColumnName() ) + " ");
                        createSQL.append(column.getDataType() + ending);
                    } else {
                        if (column.isKey()) {
                            pkList.add(getLProtectedChar( column.getColumnName() ) + column.getColumnName() + getRProtectedChar( column.getColumnName() ));
                        }
                        createSQL.append(getLProtectedChar( column.getColumnName() ) + column.getColumnName() + getRProtectedChar( column.getColumnName() ) + " ");
                        String dataType = null;
                        if (column.getColumn().getType() == null || column.getColumn().getType().trim().equals("")) {
                            dataType = mappingType.getDefaultSelectedDbType(column.getColumn().getTalendType());
                        } else {
                            dataType = column.getColumn().getType();
                        }
                        if ("mysql_id".equalsIgnoreCase(getDBMSId()) && dataType.endsWith("UNSIGNED")) {                            
                            createSQL.append(dataType.substring(0,dataType.indexOf("UNSIGNED"))) ;                            
                        }else {                            
                            createSQL.append(dataType);
                        }
                        Integer length = column.getColumn().getLength() == null ? 0 : column.getColumn().getLength();
                        Integer precision = column.getColumn().getPrecision() == null ? 0 : column.getColumn().getPrecision();
                        boolean lengthIgnored = mappingType.isLengthIgnored(getDBMSId(), dataType);
                        boolean precisionIgnored = mappingType.isPrecisionIgnored(getDBMSId(), dataType);
                        String prefix = "";
                        String suffix = "";
                        String comma = "";
                        
                        if ( ("oracle_id".equalsIgnoreCase(getDBMSId()))
                                && (("NUMBER".equalsIgnoreCase(dataType)) || ("CHAR".equalsIgnoreCase(dataType)) || ("NCHAR".equalsIgnoreCase(dataType)))
                                && (column.getColumn().getLength() == null || 0 == column.getColumn().getLength())
                                && (column.getColumn().getPrecision() == null || 0 == column.getColumn().getPrecision())
                        ){} 
                        else if (("mysql_id".equalsIgnoreCase(getDBMSId()))
                                && (("DECIMAL".equalsIgnoreCase(dataType)) || ("NUMERIC".equalsIgnoreCase(dataType)))
                                && (column.getColumn().getLength() == null || 0 == column.getColumn().getLength())
                                && (column.getColumn().getPrecision() == null || 0 == column.getColumn().getPrecision())
                        ) {}
                        else {
                            if (mappingType.isPreBeforeLength(getDBMSId(), dataType)) {
                                if (!precisionIgnored) {
                                    prefix = "(";
                                    suffix = ") ";
                                    createSQL.append(prefix + precision);
                                }
                                if (!lengthIgnored) {
                                    prefix = (prefix.equals("") ? "(" : prefix);
                                    suffix = (suffix.equals("") ? ") " : suffix);
                                    if (precisionIgnored) {
                                        createSQL.append(prefix);
                                        comma = "";
                                    } else {
                                        comma = ",";
                                    }
                                    createSQL.append(comma + length);
                                }
                                createSQL.append(suffix);
                            } else {
                                if (!lengthIgnored) {
                                    if (("postgres_id".equalsIgnoreCase(getDBMSId()) || "postgresplus_id".equalsIgnoreCase(getDBMSId()) ) && column.getColumn().getLength() == null) {                                    
                                    } else { 
                                        prefix = "(";
                                        suffix = ") ";
                                        createSQL.append(prefix + length);                                    
                                    }
                                }
                                if (!precisionIgnored) {
                                    prefix = (prefix.equals("") ? "(" : prefix);
                                    suffix = (suffix.equals("") ? ") " : suffix);
                                    if (lengthIgnored) {
                                        createSQL.append(prefix);
                                        comma = "";
                                    } else {
                                        comma = ",";
                                    }
                                    createSQL.append(comma + precision);
                                }
                                if (("postgres_id".equalsIgnoreCase(getDBMSId()) || "postgresplus_id".equalsIgnoreCase(getDBMSId()) ) && column.getColumn().getLength() == null) {                                
                                } else {
                                    createSQL.append(suffix);
                                }
                                if("mysql_id".equalsIgnoreCase(getDBMSId()) && dataType.endsWith("UNSIGNED")) {
                                    createSQL.append("UNSIGNED");
                                }
                            }                            
                            
                        }
                        if(column.isAutoIncrement()) {
                            createSQL.append(getAutoIncrement(column.getStartValue(), column.getStep()));
                        } else {
                            createSQL.append(setDefaultValue(column.getColumn().getDefault(), dataType));
                            createSQL.append(setNullable(column.getColumn().isNullable()));
                        }
                        if (count == columnList.size() - 1 && pkList.size() == 0) {
                            ending = "";
                        }
                        createSQL.append(ending);
                    }
                }
                count++;
            }
            if (pkList.size() > 0) {
                createSQL.append("primary key(");
                int i = 0;
                for (String pk : pkList) {
                    createSQL.append(pk);
                    if (i != pkList.size() - 1) {
                        createSQL.append(",");
                    }
                    i++;
                }
                createSQL.append(")");
            }
            //createSQL.append(")");
            
            // add VectorWise option
            //if ("vectorwise_id".equalsIgnoreCase(getDBMSId())){
            //	createSQL.append("WITH STRUCTURE = VECTORWISE");
            //}
            
            return createSQL.toString();
        }
        protected String getAutoIncrement(int startValue, int step) {
            return " IDENTITY (" + startValue + ", " + step + ") NOT NULL";
        }
        protected String setNullable(boolean nullable) {
            if(!nullable) {
                return " not null ";
            } else {
                return "";
            }
        }
        protected String setDefaultValue(String defaultValue, String columnType) {
            if (defaultValue == null || defaultValue.equals("\"\"") || defaultValue.equals("")) {
                return " ";
            } else if ((defaultValue.startsWith("\"") || defaultValue.startsWith("'"))
                    && (defaultValue.endsWith("\"") || defaultValue.endsWith("'"))) {
                return " default '" + defaultValue.substring(1, defaultValue.length() - 1) + "' ";
            } else if (defaultValue.equalsIgnoreCase("null")) {
                return " default null ";
            } else {
                return " default " + defaultValue + " ";
            }
        }
        public String getUpdateBulkSQL(List<IMetadataColumn> columnList) {
            StringBuilder updateBulkSQL = new StringBuilder();
            StringBuilder updateSetStmt = new StringBuilder();
            StringBuilder updateWhereStmt = new StringBuilder();
            updateBulkSQL.append("UPDATE " + getLProtectedChar() + "\" +  tableName_" + cid + " + \"" + getRProtectedChar() + ", " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar());
            boolean firstKeyColumn = true;
            boolean firstUpdateColumn = true;
            String keySeparator = null;
            String updateSeparator = null;
            for(IMetadataColumn column : columnList) {
                if(column.isKey()) {
                    if(firstKeyColumn) {
                        keySeparator = "";
                        firstKeyColumn = false;
                        updateWhereStmt.append(" WHERE \" + \"");
                    } else {
                        keySeparator = " AND ";
                    }
                    updateWhereStmt.append(keySeparator);                    
                    updateWhereStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                } else {
                    if(firstUpdateColumn) {
                        updateSeparator = "";
                        firstUpdateColumn = false;
                        updateSetStmt.append(" SET \" + \"");
                    } else {
                        updateSeparator = ", ";
                    }
                    updateSetStmt.append(updateSeparator);
                    updateSetStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                }
            }
            return updateBulkSQL.toString() + updateSetStmt.toString() + updateWhereStmt.toString();
        }

        public List<Column> createColumnList(List<IMetadataColumn> columnList, List<Map<String, String>> addCols) {
            List<Column> stmtStructure = new ArrayList<Column>();
            for(IMetadataColumn column : columnList) {
                Column skeletonColumn = getColumn(column);
                skeletonColumn.setOperator("=");
                stmtStructure.add(skeletonColumn);
            }
            for(IMetadataColumn column : columnList) {
                if(addCols != null && addCols.size() > 0) {
                    for(Map<String, String> additionColumn : addCols) {
                        if(additionColumn.get("REFCOL").equals(column.getLabel())) {
                            int stmtIndex = 0;      
                            for(Column stmtStr : stmtStructure){          
                                if(stmtStr.getName().equals(additionColumn.get("REFCOL"))) {
                                    break;
                                }
                                stmtIndex++;
                            }           
                            if(additionColumn.get("POS").equals("AFTER")) {
                                Column insertAfter = getColumn("\" + " + additionColumn.get("NAME") + " + \"", "\" + " + additionColumn.get("SQL") + " + \"", true);
                                insertAfter.setDataType(additionColumn.get("DATATYPE"));
                                insertAfter.setOperator("=");
                                stmtStructure.add(stmtIndex+1, insertAfter);
                            } else if(additionColumn.get("POS").equals("BEFORE")) {
                                Column insertBefore = getColumn("\" + " + additionColumn.get("NAME") + " + \"", "\" + " + additionColumn.get("SQL") + " + \"", true);
                                insertBefore.setDataType(additionColumn.get("DATATYPE"));
                                insertBefore.setOperator("=");
                                stmtStructure.add(stmtIndex, insertBefore);
                            } else if(additionColumn.get("POS").equals("REPLACE")) {
                                Column replacementCol = getColumn("\" + " + additionColumn.get("NAME") + " + \"", "\" + " + additionColumn.get("SQL") + " + \"", true);
                                replacementCol.setDataType(additionColumn.get("DATATYPE"));
                                replacementCol.setOperator("=");
                                Column replacedCol = (Column) stmtStructure.get(stmtIndex);
                                replacedCol.replace(replacementCol);
                            }
                        }                        
                    }
                }
            }
            return stmtStructure;
        }
        public List<Column> createColumnList(List<IMetadataColumn> columnList, boolean useFieldOptions, List<Map<String, String>> fieldOptions, List<Map<String, String>> addCols, boolean isSpecifyIdentityKey, String identityKey, int startValue, int step) {
            List<Column> stmtStructure = createColumnList(columnList, useFieldOptions, fieldOptions, addCols);
            if(isSpecifyIdentityKey) {
                for(Column column : stmtStructure) {
                    if(column.getColumnName().equals(identityKey)) {
                        column.setAutoIncrement(true);
                        column.setStartValue(startValue);
                        column.setStep(step);
                        break;
                    }
                }
            }
            return stmtStructure;
        }
        public List<Column> createColumnList(List<IMetadataColumn> columnList, boolean useFieldOptions, List<Map<String, String>> fieldOptions, List<Map<String, String>> addCols) {
            List<Column> stmtStructure = new ArrayList<Column>();            
            for(IMetadataColumn column : columnList) {
                Map<String, String> fieldOption = null;
                if(fieldOptions != null && fieldOptions.size() > 0) {
                    for(Map<String, String> tmpFieldOption : fieldOptions) {
                        if(column.getLabel().equals(tmpFieldOption.get("SCHEMA_COLUMN"))) {
                            fieldOption = tmpFieldOption;
                            break;
                        }
                    }
                }
				if(!("id_Dynamic".equals(column.getTalendType()))) {
					Column skeletonColumn = getColumn(column, column.isKey(), useFieldOptions, fieldOption);
					skeletonColumn.setOperator("=");
					skeletonColumn.setDataType(column.getType());
					stmtStructure.add(skeletonColumn);
				}
            }
            if(addCols != null && addCols.size() > 0) {
                for(IMetadataColumn column : columnList) {
                    for(Map<String, String> additionColumn : addCols) {
                        if(additionColumn.get("REFCOL").equals(column.getLabel())) {
                            int stmtIndex = 0;
                            for(Column stmtStr : stmtStructure){          
                                if(stmtStr.getName().equals(additionColumn.get("REFCOL"))) {
                                    break;
                                }
                                stmtIndex++;
                            }           
                            if(additionColumn.get("POS").equals("AFTER")) {
                                Column insertAfter = getColumn("\" + " + additionColumn.get("NAME") + " + \"", "\" + " + additionColumn.get("SQL") + " + \"", true);
                                insertAfter.setDataType(additionColumn.get("DATATYPE"));
                                insertAfter.setOperator("=");
                                stmtStructure.add(stmtIndex+1, insertAfter);
                            } else if(additionColumn.get("POS").equals("BEFORE")) {
                                Column insertBefore = getColumn("\" + " + additionColumn.get("NAME") + " + \"", "\" + " + additionColumn.get("SQL") + " + \"", true);
                                insertBefore.setDataType(additionColumn.get("DATATYPE"));
                                insertBefore.setOperator("=");
                                stmtStructure.add(stmtIndex, insertBefore);
                            } else if(additionColumn.get("POS").equals("REPLACE")) {
                                Column replacementCol = getColumn("\" + " + additionColumn.get("NAME") + " + \"", "\" + " + additionColumn.get("SQL") + " + \"", true);
                                replacementCol.setDataType(additionColumn.get("DATATYPE"));
                                replacementCol.setOperator("=");                                
                                Column replacedCol = (Column) stmtStructure.get(stmtIndex);
                                replacementCol.setKey(replacedCol.isKey());
                                replacementCol.setUpdateKey(replacedCol.isUpdateKey());
                                replacementCol.setDeleteKey(replacedCol.isDeleteKey());
                                replacementCol.setUpdatable(replacedCol.isUpdatable());
                                replacementCol.setInsertable(replacedCol.isInsertable());
                                replacedCol.replace(replacementCol);
                            }                            
                        }
                    }
                }
            }
            return stmtStructure;
        }
        public Map<String, StringBuilder> createProcessSQL(List<Column> stmtStructure) {
            Map<String, StringBuilder> actionSQLMap = new HashMap<String, StringBuilder>();
            if(stmtStructure==null || stmtStructure.size() < 1) {
            	actionSQLMap.put(INSERT_COLUMN_NAME, new StringBuilder());
            	actionSQLMap.put(INSERT_VALUE_STMT, new StringBuilder());
            	actionSQLMap.put(UPDATE_SET_STMT, new StringBuilder());
            	actionSQLMap.put(UPDATE_WHERE_STMT, new StringBuilder());
            	actionSQLMap.put(DELETE_WHERE_STMT, new StringBuilder());
            	actionSQLMap.put(FIRST_UPDATE_KEY, new StringBuilder());
            	actionSQLMap.put(FIRST_DELETE_KEY, new StringBuilder());
            	actionSQLMap.put(FIRST_INSERT_COLUMN, new StringBuilder());
            	actionSQLMap.put(FIRST_UPDATE_COLUMN, new StringBuilder());
            } else {
	            for(Column column : stmtStructure) {
	            	if(column.isReplaced()) {
	                    List<Column> replacedColumns = column.getReplacement();
	                    for(Column replacedColumn : replacedColumns) {
	                        actionSQLMap = processSQLClause(replacedColumn, actionSQLMap);
	                    }
	                } else {
	                    actionSQLMap = processSQLClause(column, actionSQLMap);        
	                }
	            }
            }
            return actionSQLMap;
        }
        private Map<String, StringBuilder> processSQLClause(Column column, Map<String, StringBuilder> actionSQLMap) {
            StringBuilder insertColName = actionSQLMap.get(INSERT_COLUMN_NAME);
            if(insertColName == null) {
                insertColName = new StringBuilder();
            }
            StringBuilder insertValueStmt = actionSQLMap.get(INSERT_VALUE_STMT);
            if(insertValueStmt == null) {
                insertValueStmt = new StringBuilder();
            }
            StringBuilder updateSetStmt = actionSQLMap.get(UPDATE_SET_STMT);
            if(updateSetStmt == null) {
                updateSetStmt = new StringBuilder(); 
            }
            StringBuilder updateWhereStmt = actionSQLMap.get(UPDATE_WHERE_STMT);
            if(updateWhereStmt == null) {
                updateWhereStmt = new StringBuilder();
            }
            StringBuilder deleteWhereStmt = actionSQLMap.get(DELETE_WHERE_STMT);
            if(deleteWhereStmt == null) {
                deleteWhereStmt = new StringBuilder();
            }
            StringBuilder firstUpdateKeyColumn = actionSQLMap.get(FIRST_UPDATE_KEY);
            if(firstUpdateKeyColumn == null) {
                firstUpdateKeyColumn = new StringBuilder("true");
            }
            StringBuilder firstDeleteKeyColumn = actionSQLMap.get(FIRST_DELETE_KEY);
            if(firstDeleteKeyColumn == null) {
                firstDeleteKeyColumn = new StringBuilder("true");
            }
            StringBuilder firstInsertColumn = actionSQLMap.get(FIRST_INSERT_COLUMN);
            if(firstInsertColumn == null) {
                firstInsertColumn = new StringBuilder("true");
            }
            StringBuilder firstUpdateColumn = actionSQLMap.get(FIRST_UPDATE_COLUMN);
            if(firstUpdateColumn == null) {
                firstUpdateColumn = new StringBuilder("true");
            }
            String suffix = null;
            String separate = null;
            if(column.isInsertable()) {
                if(firstInsertColumn.toString().equals("true")) {
                    suffix = "";
                    firstInsertColumn = new StringBuilder("false");
                } else {
                    suffix = ",";
                }
                insertColName.append(suffix);
                insertColName.append(getLProtectedChar(column.getColumnName()) + column.getColumnName() + getRProtectedChar(column.getColumnName()));
                insertValueStmt.append(suffix);
                insertValueStmt.append(column.getSqlStmt());
            }
            if(column.isUpdatable()) {
                if(firstUpdateColumn.toString().equals("true")) {
                    suffix = "";
                    firstUpdateColumn = new StringBuilder("false");
                } else {
                    suffix = ",";
                }
                updateSetStmt.append(suffix);
                updateSetStmt.append(getLProtectedChar(column.getColumnName()) + column.getColumnName() + getRProtectedChar(column.getColumnName()) + " " + column.getOperator() + " " + column.getSqlStmt());
            }
            if(column.isDeleteKey()) {
                if(firstDeleteKeyColumn.toString().equals("true")) {
                    separate = "";
                    firstDeleteKeyColumn = new StringBuilder("false");
                } else {
                    separate = " AND ";
                }
                deleteWhereStmt.append(separate);
                
                //feature:2880
                whereStmtSupportNull(deleteWhereStmt, column);                 
            }
            if(column.isUpdateKey()) {
                if(firstUpdateKeyColumn.toString().equals("true")) {
                    separate = "";
                    firstUpdateKeyColumn = new StringBuilder("false");
                } else {
                    separate = " AND ";
                }
                updateWhereStmt.append(separate);
                
                //feature:2880
                whereStmtSupportNull(updateWhereStmt, column);                
                                            
            }
            actionSQLMap.put(INSERT_COLUMN_NAME, insertColName);
            actionSQLMap.put(INSERT_VALUE_STMT, insertValueStmt);
            actionSQLMap.put(UPDATE_SET_STMT, updateSetStmt);
            actionSQLMap.put(UPDATE_WHERE_STMT, updateWhereStmt);
            actionSQLMap.put(DELETE_WHERE_STMT, deleteWhereStmt);
            actionSQLMap.put(FIRST_UPDATE_KEY, firstUpdateKeyColumn);
            actionSQLMap.put(FIRST_DELETE_KEY, firstDeleteKeyColumn);
            actionSQLMap.put(FIRST_INSERT_COLUMN, firstInsertColumn);
            actionSQLMap.put(FIRST_UPDATE_COLUMN, firstUpdateColumn);
            return actionSQLMap;
        }
        public String getGenerateType(String typeToGenerate) {
            if(typeToGenerate.equals("byte[]")) {
                typeToGenerate = "Bytes";
            } else if(typeToGenerate.equals("java.util.Date")) {
                typeToGenerate = "Date";
            } else if(typeToGenerate.equals("Integer")) {
                typeToGenerate = "Int";
            } else if(typeToGenerate.equals("List")) {  
                typeToGenerate = "Object";                 
            } else {
                typeToGenerate=typeToGenerate.substring(0,1).toUpperCase()+typeToGenerate.substring(1);
            }
            return typeToGenerate;
        }
		
		public String generateSetStmt(String typeToGenerate, String dbType, Column column, int index, String incomingConnName, String cid, int actionType) {
			return generateSetStmt(typeToGenerate, dbType, column, index, incomingConnName, cid, actionType, null);	
		}
		
        public String generateSetStmt(String typeToGenerate, String dbType, Column column, int index, String incomingConnName, String cid, int actionType, String dynamic) {
			
			if(dynamic==null) {
				dynamic="";
			} else {
				dynamic+=cid;
			}
			
            boolean isObject = false;
            String prefix = null;
            if(actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if(actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if(actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            if(typeToGenerate.equals("Character")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.CHAR);\r\n");                
			} else if(typeToGenerate.equals("Date")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.DATE);\r\n");                
            } else if(typeToGenerate.equals("byte[]")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                if(dbType != null && (dbType.equals("LONG RAW") || dbType.equals("RAW"))) {
                    setStmt.append(prefix + cid + ".setBytes(" + index + dynamic + ", null);\r\n");
                } else {
                    setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.ARRAY);\r\n");                    
                }                
            } else if(typeToGenerate.equals("Long") || typeToGenerate.equals("Byte") || typeToGenerate.equals("Integer") || typeToGenerate.equals("Short")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.INTEGER);\r\n");                
            } else if(typeToGenerate.equals("String")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                if(dbType != null && dbType.equals("CLOB")) {
                    setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.CLOB);\r\n");                    
                } else {
                    setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.VARCHAR);\r\n");                    
                }                
            } else if(typeToGenerate.equals("Object")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                if(dbType != null && dbType.equals("BLOB")) {
                    setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.BLOB);\r\n");
        		} else if("CLOB".equals(dbType)){
                    setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.CLOB);\r\n");
                } else {
                    setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.OTHER);\r\n");                    
                }               
            } else if(typeToGenerate.equals("Boolean")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.BOOLEAN);\r\n");                
            } else if(typeToGenerate.equals("Double")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.DOUBLE);\r\n");                
            } else if(typeToGenerate.equals("Float")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.FLOAT);\r\n");                
            }
            if(isObject) {
                setStmt.append("} else {");
            }
            typeToGenerate = getGenerateType(typeToGenerate);
            if(typeToGenerate.equals("Char") || typeToGenerate.equals("Character")) {
                if(isObject) {
                    setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n"); 
                } else {
                    setStmt.append("if(String.valueOf(" + incomingConnName + "." + column.getName() + ").toLowerCase().equals(\"null\")) {\r\n");
                }
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.CHAR);\r\n");
                setStmt.append("} else if(" + incomingConnName + "." + column.getName() + " == '\0'){\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + dynamic + ", \"\");\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + dynamic + ", String.valueOf(" + incomingConnName + "." + column.getName() + "));\r\n");
                setStmt.append("}");
            } else if(typeToGenerate.equals("Date")) {
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " != null) {\r\n");
                setStmt.append(prefix + cid + ".setTimestamp(" + index + dynamic + ", new java.sql.Timestamp(" + incomingConnName + "." + column.getName() + ".getTime()));\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.DATE);\r\n");
                setStmt.append("}\r\n");
            } else if(typeToGenerate.equals("Bytes") && (dbType != null && (dbType.equals("LONG RAW") || dbType.equals("RAW")))) {
                setStmt.append(prefix + cid + ".setBytes(" + index + dynamic + ", " + incomingConnName + "." + column.getName() + ");\r\n");
            } else if(typeToGenerate.equals("String") && (dbType != null && dbType.equals("CLOB"))) {
                setStmt.append(prefix + cid + ".setCharacterStream(" + index + dynamic + ", new java.io.StringReader(" + incomingConnName + "." + column.getName() + "), " + incomingConnName + "." + column.getName() + ".length());\r\n");                
            } else if (typeToGenerate.equals("Bytes") && (dbType != null && dbType.equals("BLOB"))) {
                setStmt.append(prefix + cid + ".setBinaryStream(" + index + dynamic + ", new java.io.ByteArrayInputStream((byte[])" + incomingConnName + "." + column.getName() + "), ((byte[])" + incomingConnName + "." + column.getName() + ").length);\r\n");
            }
            else {
                setStmt.append(prefix + cid + ".set" + typeToGenerate + "(" + index + dynamic + ", " + incomingConnName + "." + column.getName() + ");\r\n");
            }
            if(isObject) {
                setStmt.append("}\r\n");
            }
            return setStmt.toString();            
        }
		
		public String generateSetStmt(String typeToGenerate, Column column, int index, String incomingConnName, String cid, int actionType) {
			return generateSetStmt(typeToGenerate, column, index, incomingConnName, cid, actionType, null);	
		}
		
        public String generateSetStmt(String typeToGenerate, Column column, int index, 
                String incomingConnName, String cid, int actionType, String dynamic) {
			
			if(dynamic==null) {
				dynamic="";
			} else {
				dynamic+=cid;
			}
			
            boolean isObject = false;
            String prefix = null;
            if(actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if(actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if(actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            if(typeToGenerate.equals("Character")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.CHAR);\r\n");                
            } else if(typeToGenerate.equals("Date")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.DATE);\r\n");                
            } else if(typeToGenerate.equals("byte[]")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                if(column.column != null && ("BINARY".equals(column.column.getType()) || "VARBINARY".equals(column.column.getType()))) {
                	setStmt.append(prefix + cid + ".setBytes(" + index + dynamic + ", null);\r\n");
                } else {
                	setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.ARRAY);\r\n");                
            	}
            } else if(typeToGenerate.equals("Long") || typeToGenerate.equals("Byte") || typeToGenerate.equals("Integer") || typeToGenerate.equals("Short")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.INTEGER);\r\n");                
            } else if(typeToGenerate.equals("String")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.VARCHAR);\r\n");                
            } else if(typeToGenerate.equals("Object")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                if(column.column != null && ("BINARY".equals(column.column.getType()) || "VARBINARY".equals(column.column.getType()))) {
                	setStmt.append(prefix + cid + ".setBytes(" + index + dynamic + ", null);\r\n");
                } else {
                	setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.OTHER);\r\n");                
            	}               
            } else if(typeToGenerate.equals("Boolean")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.BOOLEAN);\r\n");                
            } else if(typeToGenerate.equals("Double")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.DOUBLE);\r\n");                
            } else if(typeToGenerate.equals("Float")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.FLOAT);\r\n");                
            }
            if(isObject) {
                setStmt.append("} else {");
            }
            typeToGenerate = getGenerateType(typeToGenerate);
            if(typeToGenerate.equals("Char") || typeToGenerate.equals("Character")) {
                if(isObject) {
                    setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n"); 
                } else {
                    setStmt.append("if(String.valueOf(" + incomingConnName + "." + column.getName() + ").toLowerCase().equals(\"null\")) {\r\n");
                }
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.CHAR);\r\n");
                setStmt.append("} else if(" + incomingConnName + "." + column.getName() + " == '\0'){\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + dynamic + ", \"\");\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + dynamic + ", String.valueOf(" + incomingConnName + "." + column.getName() + "));\r\n");
                setStmt.append("}");
            } else if(typeToGenerate.equals("Date")) {
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " != null) {\r\n");
                setStmt.append(prefix + cid + ".setTimestamp(" + index + dynamic + ", new java.sql.Timestamp(" + incomingConnName + "." + column.getName() + ".getTime()));\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + dynamic + ", java.sql.Types.DATE);\r\n");
                setStmt.append("}\r\n");
            } else {
                setStmt.append(prefix + cid + ".set" + typeToGenerate + "(" + index + dynamic + ", " + incomingConnName + "." + column.getName() + ");\r\n");
            }
            if(isObject) {
                setStmt.append("}\r\n");
            }
            return setStmt.toString();
        }
        
        // @Deprecated : see bug8551
        public String retrieveSQL(String generatedType, Column column, String incomingConnName, String cid, String stmt) {
            String replaceStr = null;
            if(generatedType.equals("char") || generatedType.equals("Character")) {
                replaceStr = "\"'\" + String.valueOf(" + incomingConnName + "." + column.getName() + ") + \"'\"";
            } else if(generatedType.equals("String")) {
                replaceStr = "\"'\" + " + incomingConnName + "." + column.getName() + " + \"'\"";
            } else if(generatedType.equals("java.util.Date")) {
                replaceStr = "\"'\" + new java.text.SimpleDateFormat(" + column.getColumn().getPattern() + ").format(" + incomingConnName + "." + column.getName() + ") + \"'\"";
                
            } else {
                replaceStr = "String.valueOf(" + incomingConnName + "." + column.getName() + ")";
            }
            
            
            if (generatedType.equals("int") || generatedType.equals("float") || generatedType.equals("double") ||generatedType.equals("long") 
                    || generatedType.equals("short") || generatedType.equals("boolean") || generatedType.equals("byte") || generatedType.equals("char")) {
                return stmt + cid + ".replaceFirst(\"\\\\?\", " +  replaceStr + ")";
            }else {
                return stmt + cid + ".replaceFirst(\"\\\\?\", " + incomingConnName + "." + column.getName() +"== null ?  \"null\" :" + replaceStr + ")";
            }
            
                
        }
        
        public String retrieveSQL(String generatedType, Column column, String incomingConnName, String cid, String stmt, int index, String sqlSplit) {
            String replaceStr = null;
            if(generatedType.equals("char") || generatedType.equals("Character")) {
                replaceStr = "\"'\" + String.valueOf(" + incomingConnName + "." + column.getName() + ") + \"'\"";
            } else if(generatedType.equals("String")) {
                replaceStr = "\"'\" + " + incomingConnName + "." + column.getName() + " + \"'\"";
            } else if(generatedType.equals("java.util.Date")) {
                replaceStr = "\"'\" + new java.text.SimpleDateFormat(" + column.getColumn().getPattern() + ").format(" + incomingConnName + "." + column.getName() + ") + \"'\"";
                
            } else {
                replaceStr = "String.valueOf(" + incomingConnName + "." + column.getName() + ")";
            }
            
            if (generatedType.equals("int") || generatedType.equals("float") || generatedType.equals("double") ||generatedType.equals("long") 
                    || generatedType.equals("short") || generatedType.equals("boolean") || generatedType.equals("byte") || generatedType.equals("char")) {
            		if (index == 1) {
            			return  stmt + cid + ".append("+sqlSplit+cid+"[0]).append(" +  replaceStr + ").append("+sqlSplit+cid+"["+index+"])";
            		} else {
            			return  stmt + cid + ".append(" +  replaceStr + ").append("+sqlSplit+cid+"["+index+"])";
            		}
            }else {
            	if (index == 1) {
            		return stmt + cid + ".append("+sqlSplit+cid+"[0]).append(" + incomingConnName + "." + column.getName() +"== null ?  \"null\" :" + replaceStr + ").append("+sqlSplit+cid+"["+index+"])";
            	} else {
            		return stmt + cid + ".append(" + incomingConnName + "." + column.getName() +"== null ?  \"null\" :" + replaceStr + ").append("+sqlSplit+cid+"["+index+"])";
            	}
            }
        }
        
        //feature:2880 @6980 in debug mode
        public String retrieveSQL(String generatedType, Column column, String incomingConnName, String cid, String stmt, int index, String sqlSplit, String replaceFixedStr) {
        	if (index == 1) {
        		return stmt + cid + ".append("+sqlSplit+cid+"[0]).append(" +replaceFixedStr+ ").append("+sqlSplit+cid+"["+index+"])";
        	} else {
        		return stmt + cid + ".append(" +replaceFixedStr+").append("+sqlSplit+cid+"["+index+"])";
        	}
        }
        
        //extract a method for feature:2880, it is a default implement, need every tDBOutput to implement it, if it really want to support the "whereNULL" issue
        //@see: the implement of MSSQLManager
        //feature:2880 @6980
        //i.e: 
        //1.select * FROM user where ((true = true AND name is NULL) OR name = ?);
        //2.select * FROM user where ((true = false AND name is NULL) OR name = ?);
        
        public void whereStmtSupportNull(StringBuilder updateWhereStmt, Column column) {
            boolean whereSupportNull = false;

            //if node = null, it means some components have not support the "whereNULL" issue yet.
            if (node != null) {
                whereSupportNull = ElementParameterParser.getValue(node, "__SUPPORT_NULL_WHERE__").equals("true");
            }
            if (whereSupportNull && column.getColumn().isNullable()) {
                updateWhereStmt.append("((" + getLProtectedChar(column.getColumnName()) + column.getColumnName() + getRProtectedChar(column.getColumnName())
                        + " IS NULL AND " + getColumnIsNullCondition() +" " + column.getOperator() + " " + column.getSqlStmt() + ") ");
                updateWhereStmt.append("OR " + getLProtectedChar(column.getColumnName()) + column.getColumnName() + getRProtectedChar(column.getColumnName())
                        + column.getOperator() + column.getSqlStmt() + ")");
            } else {
                //if node = null, go this branch as the old behave
                updateWhereStmt.append(getLProtectedChar(column.getColumnName()) + column.getColumnName() + getRProtectedChar(column.getColumnName()) + " "
                        + column.getOperator() + " " + column.getSqlStmt());
            }
        }
        
        //feature:2880 @6980
        //need all db to implement, return the column whether is null(in java) prefix condition.
        protected String getColumnIsNullCondition() {
        	return "1";
        }
        
        // for feature:2880, it will consider the "whereNULL" issue in the generated code.
        // @Deprecated: because the "index" with a small problem when increase it.
        private String generateSetStmt_4_whereSupportNull(String typeToGenerate, Column column, int index,
                String incomingConnName, String cid, int actionType) {

            boolean whereSupportNull = false;

            // if node = null, it means some components have not support the "whereNULL" issue yet.
            if (node != null) {
                whereSupportNull = ElementParameterParser.getValue(node, "__SUPPORT_NULL_WHERE__").equals("true");
            }

            StringBuilder setStmt = new StringBuilder();

            System.out.println(column.isUpdateKey() && whereSupportNull && column.getColumn().isNullable());
            // the 3 conditions are important
            if (column.isUpdateKey() && whereSupportNull && column.getColumn().isNullable()) {
                setStmt.append(generateSetBooleanForNullableKeyStmt(column, index, incomingConnName, cid, NORMAL_TYPE));
            }

            // the old behave
            setStmt.append(generateSetStmt(typeToGenerate, column, index, incomingConnName, cid, NORMAL_TYPE));

            return setStmt.toString();
        }

        // for feature:2880 @6980, generate the "set XXXX" java code.thie code is about  the column whether is null(in java) prefix condition.
        //@see:getColumnIsNullCondition()
        //need all db to implement.
        protected String generateSetBooleanForNullableKeyStmt(Column column, int index, String incomingConnName, String cid,
                int actionType) {
            String prefix = null;
            if (actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if (actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if (actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            //TODO generate setXXXX code according to each db .
            setStmt.append(prefix + cid + ".setInt(" + index + ",  ((" + incomingConnName + "." + column.getName() + "==null)?1:0));\r\n");
            return setStmt.toString();
        }     
        
		public String getCopyFromCSVSQL(List<IMetadataColumn> columnList, 
				String fieldDelimiter, 
				String newLineChar,
				String nullIndicator) {
		return null;
			
		}
		
		public String generateCode4TabelExist() {
		    boolean useExistingConnection = "true".equals(ElementParameterParser.getValue(node,"__USE_EXISTING_CONNECTION__"));
		    String connection = ElementParameterParser.getValue(node,"__CONNECTION__");
		    StringBuilder code = new StringBuilder();  
		    code.append("String tableNameForSearch_" + cid + "= " + getTableName4Search(useExistingConnection, connection) + ";\r\n");
		    if (hasSchema()) {
    		    code.append("String dbschemaForSearch_" + cid + "= null;\r\n");
    		    code.append("if(dbschema_" + cid + "== null || dbschema_" + cid + ".trim().length() == 0) {\r\n");
    		    code.append("dbschemaForSearch_" + cid + "= " + getUserName4Search(useExistingConnection, connection)+ ";\r\n");
    		    code.append("} else {\r\n");
    		    code.append("dbschemaForSearch_" + cid + "= " + getShemaName4Search(useExistingConnection, connection)+ ";\r\n");
    		    code.append("}\r\n");
		    }
		    return code.toString();
		}
		
		protected String getTableName4Search(boolean useExistingConnection, String connection) {
		    return "\""+getLProtectedChar()+ "\" + " + ElementParameterParser.getValue(node,"__TABLE__") +" + \"" + getRProtectedChar() + "\""; 
		}
		
	    protected String getUserName4Search(boolean useExistingConnection, String connection) {
            return "";   
	    }

	    /*
	     * maybe some db need add getLProtectedChar() and getRProtectedChar() to schema name.
	     * this because of some db default add getLProtectedChar() and getRProtectedChar() to schaem when create table. e.g:db2
	     * 
	     * in fact the db add getLProtectedChar() and getRProtectedChar() to scheam when create table that is wrong
	    */
        protected String getShemaName4Search(boolean useExistingConnection, String connection) {
            return "";   
        }	
        
        protected boolean hasSchema() {
            return false;
        }
	    
    }
    
    public class AS400Manager extends Manager {
        public AS400Manager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return AS400;
        }
        protected String getLProtectedChar() {
            return "";
        }
        protected String getRProtectedChar() {
            return "";
        }
        
        //feature:2880 @6980
        //need all db to implement, return the column whether is null(in java) prefix condition.
        protected String getColumnIsNullCondition() {
        	return "1";
        }
        
        // for feature:2880 @6980, generate the "set XXXX" java code.thie code is about  the column whether is null(in java) prefix condition.
        //@see:getColumnIsNullCondition()
        //need all db to implement.
        protected String generateSetBooleanForNullableKeyStmt(Column column, int index, String incomingConnName, String cid,
                int actionType) {
            String prefix = null;
            if (actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if (actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if (actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            setStmt.append(prefix + cid + ".setInt(" + index + ",  ((" + incomingConnName + "." + column.getName()
                    + "==null)?1:0));\r\n");
            return setStmt.toString();
        } 
    }
    
    public class AccessManager extends Manager {
        public AccessManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return ACCESS;
        }
        protected String getLProtectedChar() {
            return "[";
        }
        protected String getRProtectedChar() {
            return "]";
        }   
        
        public String generateSetStmt(String typeToGenerate, Column column, int index, 
                String incomingConnName, String cid, int actionType) {
            boolean isObject = false;
            String prefix = null;
            if(actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if(actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if(actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            if(typeToGenerate.equals("Character")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");                
            } else if(typeToGenerate.equals("Date")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DATE);\r\n");                
            } else if(typeToGenerate.equals("byte[]")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.ARRAY);\r\n");                
            } else if(typeToGenerate.equals("Long") || typeToGenerate.equals("Byte") || typeToGenerate.equals("Integer") || typeToGenerate.equals("Short")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.INTEGER);\r\n");                
            } else if(typeToGenerate.equals("String")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.VARCHAR);\r\n");                
            } else if(typeToGenerate.equals("Object")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.OTHER);\r\n");                
            } else if(typeToGenerate.equals("Boolean")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.BOOLEAN);\r\n");                
            } else if(typeToGenerate.equals("Double")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DOUBLE);\r\n");                
            } else if(typeToGenerate.equals("Float")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.FLOAT);\r\n");                
            }
            if(isObject) {
                setStmt.append("} else {");
            }
            typeToGenerate = getGenerateType(typeToGenerate);
            if(typeToGenerate.equals("Char") || typeToGenerate.equals("Character")) {
                if(isObject) {
                    setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n"); 
                } else {
                    setStmt.append("if(String.valueOf(" + incomingConnName + "." + column.getName() + ").toLowerCase().equals(\"null\")) {\r\n");
                }
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");
                setStmt.append("} else if(" + incomingConnName + "." + column.getName() + " == '\0'){\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", \"\");\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", String.valueOf(" + incomingConnName + "." + column.getName() + "));\r\n");
                setStmt.append("}");
            } else if(typeToGenerate.equals("Date")) {
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " != null) {\r\n");
                setStmt.append(prefix + cid + ".setTimestamp(" + index + ", new java.sql.Timestamp(" + incomingConnName + "." + column.getName() + ".getTime()));\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DATE);\r\n");
                setStmt.append("}\r\n");
            } else {
            	if (typeToGenerate.equals("Long")) {
            		setStmt.append(prefix + cid + ".setBigDecimal(" + index + ",new BigDecimal(" + incomingConnName + "." + column.getName() + "));\r\n");
            	} else {
            		setStmt.append(prefix + cid + ".set" + typeToGenerate + "(" + index + ", " + incomingConnName + "." + column.getName() + ");\r\n");
            	}
            }
            if(isObject) {
                setStmt.append("}\r\n");
            }
            return setStmt.toString();
        }
        
        //feature:2880 @6980
        //need all db to implement, return the column whether is null(in java) prefix condition.
        protected String getColumnIsNullCondition() {
        	return "1";
        }
        
        // for feature:2880 @6980, generate the "set XXXX" java code.thie code is about  the column whether is null(in java) prefix condition.
        //@see:getColumnIsNullCondition()
        //need all db to implement.
        protected String generateSetBooleanForNullableKeyStmt(Column column, int index, String incomingConnName, String cid,
                int actionType) {
            String prefix = null;
            if (actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if (actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if (actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            setStmt.append(prefix + cid + ".setInt(" + index + ",  ((" + incomingConnName + "." + column.getName()
                    + "==null)?1:0));\r\n");
            return setStmt.toString();
        } 
        
    }
    
    public class DB2Manager extends Manager {
        public DB2Manager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return DB2;
        }
        protected String getLProtectedChar() {
            return "\\\"";
        }
        protected String getRProtectedChar() {
            return "\\\"";
        }        
        
        public String getUpdateBulkSQL(List<IMetadataColumn> columnList) {
            // try to build a sql like 
            // update customers a 
            // set (city_name,customer_type)=(select b.city_name,b.customer_type 
            //        from tmp_cust_city b 
            //        where b.customer_id=a.customer_id) // wheresub
            //        where exists (select 1 
            //        from tmp_cust_city b
            //        where b.customer_id=a.customer_id
            //        )
            StringBuilder updateBulkSQL = new StringBuilder();
            StringBuilder updateSetStmt = new StringBuilder();
            StringBuilder updateSetSelectStmt= new StringBuilder();           
            StringBuilder updateWhereSubStmt = new StringBuilder();
            StringBuilder updateWhereMainStmt = new StringBuilder();
            String keySeparator = null;
            String updateSeparator = null;
            
            boolean firstKeyColumn = true;
            boolean firstUpdateColumn = true;
            updateBulkSQL.append("UPDATE " + getLProtectedChar() + "\" +  tableName_" + cid + " + \"" + getRProtectedChar() );
            for(IMetadataColumn column : columnList) {
                if(column.isKey()) {
                    if(firstKeyColumn) {
                        keySeparator = "";
                        firstKeyColumn = false;
                        updateWhereSubStmt.append(" WHERE \" + \"");
                        updateWhereMainStmt.append(" WHERE EXISTS ( SELECT 1 FROM " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() );
                    }else {
                        keySeparator = " AND ";
                    }
                    updateWhereSubStmt.append(keySeparator);                    
                    updateWhereSubStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());                        

                }else {
                    if(firstUpdateColumn) {
                        updateSeparator = "";
                        firstUpdateColumn = false;
                        updateSetStmt.append(" SET \" + \" ( ");
                        updateSetSelectStmt.append(" = (SELECT ");
                    } else {
                        updateSeparator = ", ";
                    }
                    updateSetStmt.append(updateSeparator);
                    updateSetStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                    updateSetSelectStmt.append(updateSeparator);
                    updateSetSelectStmt.append(getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar()) ;
                }
            }
            updateSetStmt.append(" )");
            updateSetSelectStmt.append(" FROM " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar());
            updateWhereSubStmt.append(")");
            
            return updateBulkSQL.append(updateSetStmt).append(updateSetSelectStmt).append(updateWhereSubStmt).append(updateWhereMainStmt).append(updateWhereSubStmt).toString();
        }
        
        //feature:2880 @6980
        //need all db to implement, return the column whether is null(in java) prefix condition.
        protected String getColumnIsNullCondition() {
        	return "1";
        }
        
        // for feature:2880 @6980, generate the "set XXXX" java code.thie code is about  the column whether is null(in java) prefix condition.
        //@see:getColumnIsNullCondition()
        //need all db to implement.
        protected String generateSetBooleanForNullableKeyStmt(Column column, int index, String incomingConnName, String cid,
                int actionType) {
            String prefix = null;
            if (actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if (actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if (actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            setStmt.append(prefix + cid + ".setInt(" + index + ",  ((" + incomingConnName + "." + column.getName()
                    + "==null)?1:0));\r\n");
            return setStmt.toString();
        } 
        
    }
    
    public class FirebirdManager extends Manager {
        public FirebirdManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return FIREBIRD;
        }
        protected String getLProtectedChar() {
            return " ";
        }
        protected String getRProtectedChar() {
            return " ";
        }
        public String getDropTableSQL() {
            StringBuilder dropTableSQL = new StringBuilder();
            dropTableSQL.append("DROP TABLE " + getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar());
            return dropTableSQL.toString();
        }
        
         public String getCreateTableSQL(List<Column> columnList) {
            MappingTypeRetriever mappingType = MetadataTalendType.getMappingTypeRetriever(getDBMSId());
            StringBuilder createSQL = new StringBuilder();
            createSQL.append("CREATE TABLE " + getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "(");
            List<String> pkList = new ArrayList<String>();
            int count = 0;
            String ending = ",";
            for (Column column : columnList) {
                if (column.isReplaced()) {
                    List<Column> replacedColumns = column.getReplacement();
                    if (column.isKey()) {
                        for (Column replacedColumn : replacedColumns) {
                            pkList.add(getLProtectedChar(replacedColumn.getColumnName()) + replacedColumn.getColumnName() + getRProtectedChar(replacedColumn.getColumnName()));
                        }
                    }
                    int replacedCount = 0;
                    for (Column replacedColumn : replacedColumns) {
                        if (count == columnList.size() - 1 && replacedCount == replacedColumns.size() - 1 && pkList.size() == 0) {
                            ending = "";
                        }
                        createSQL.append(getLProtectedChar(replacedColumn.getColumnName()) + replacedColumn.getColumnName() + getRProtectedChar(replacedColumn.getColumnName()) + " ");
                        createSQL.append(replacedColumn.getDataType() + ending);
                        replacedCount++;
                    }
                } else {
                    if (column.isAddCol()) {
                        if (count == columnList.size() - 1 && pkList.size() == 0) {
                            ending = "";
                        }
                        createSQL.append(getLProtectedChar( column.getColumnName() ) + column.getColumnName() + getRProtectedChar( column.getColumnName() ) + " ");
                        createSQL.append(column.getDataType() + ending);
                    } else {
                        if (column.isKey()) {
                            pkList.add(getLProtectedChar( column.getColumnName() ) + column.getColumnName() + getRProtectedChar( column.getColumnName() ));
                        }
                        createSQL.append(getLProtectedChar( column.getColumnName() ) + column.getColumnName() + getRProtectedChar( column.getColumnName() ) + " ");
                        String dataType = null;
                        if (column.getColumn().getType() == null || column.getColumn().getType().trim().equals("")) {
                            dataType = mappingType.getDefaultSelectedDbType(column.getColumn().getTalendType());
                        } else {
                            dataType = column.getColumn().getType();
                        }
                        if ("mysql_id".equalsIgnoreCase(getDBMSId()) && dataType.endsWith("UNSIGNED")) {                            
                            createSQL.append(dataType.substring(0,dataType.indexOf("UNSIGNED"))) ;                            
                        }else {                            
                            createSQL.append(dataType);
                        }
                        Integer length = column.getColumn().getLength() == null ? 0 : column.getColumn().getLength();
                        Integer precision = column.getColumn().getPrecision() == null ? 0 : column.getColumn().getPrecision();
                        boolean lengthIgnored = mappingType.isLengthIgnored(getDBMSId(), dataType);
                        boolean precisionIgnored = mappingType.isPrecisionIgnored(getDBMSId(), dataType);
                        String prefix = "";
                        String suffix = "";
                        String comma = "";
                        
                        if ( ("oracle_id".equalsIgnoreCase(getDBMSId()))
                                && (("NUMBER".equalsIgnoreCase(dataType)) || ("CHAR".equalsIgnoreCase(dataType)) || ("NCHAR".equalsIgnoreCase(dataType)))
                                && (column.getColumn().getLength() == null || 0 == column.getColumn().getLength())
                                && (column.getColumn().getPrecision() == null || 0 == column.getColumn().getPrecision())
                        ){} 
                        else if (("mysql_id".equalsIgnoreCase(getDBMSId()))
                                && (("DECIMAL".equalsIgnoreCase(dataType)) || ("NUMERIC".equalsIgnoreCase(dataType)))
                                && (column.getColumn().getLength() == null || 0 == column.getColumn().getLength())
                                && (column.getColumn().getPrecision() == null || 0 == column.getColumn().getPrecision())
                        ) {}
                        else {
                            if (mappingType.isPreBeforeLength(getDBMSId(), dataType)) {
                                if (!precisionIgnored) {
                                    prefix = "(";
                                    suffix = ") ";
                                    createSQL.append(prefix + precision);
                                }
                                if (!lengthIgnored) {
                                    prefix = (prefix.equals("") ? "(" : prefix);
                                    suffix = (suffix.equals("") ? ") " : suffix);
                                    if (precisionIgnored) {
                                        createSQL.append(prefix);
                                        comma = "";
                                    } else {
                                        comma = ",";
                                    }
                                    createSQL.append(comma + length);
                                }
                                createSQL.append(suffix);
                            } else {
                                if (!lengthIgnored) {
                                    if (("postgres_id".equalsIgnoreCase(getDBMSId()) || "postgresplus_id".equalsIgnoreCase(getDBMSId()) ) && column.getColumn().getLength() == null) {                                    
                                    } else { 
                                        prefix = "(";
                                        suffix = ") ";
                                        createSQL.append(prefix + length);                                    
                                    }
                                }
                                if (!precisionIgnored) {
                                    prefix = (prefix.equals("") ? "(" : prefix);
                                    suffix = (suffix.equals("") ? ") " : suffix);
                                    if (lengthIgnored) {
                                        createSQL.append(prefix);
                                        comma = "";
                                    } else {
                                        comma = ",";
                                    }
                                    createSQL.append(comma + precision);
                                }
                                if (("postgres_id".equalsIgnoreCase(getDBMSId()) || "postgresplus_id".equalsIgnoreCase(getDBMSId()) ) && column.getColumn().getLength() == null) {                                
                                } else {
                                    createSQL.append(suffix);
                                }
                                if("mysql_id".equalsIgnoreCase(getDBMSId()) && dataType.endsWith("UNSIGNED")) {
                                    createSQL.append("UNSIGNED");
                                }
                            }                            
                            
                        }
                        if(column.isAutoIncrement()) {
                            createSQL.append(getAutoIncrement(column.getStartValue(), column.getStep()));
                        } else {
                            createSQL.append(setDefaultValue(column.getColumn().getDefault(), dataType));
                            createSQL.append(setNullable(column.getColumn().isNullable()));
                        }
                        if (count == columnList.size() - 1 && pkList.size() == 0) {
                            ending = "";
                        }
                        createSQL.append(ending);
                    }
                }
                count++;
            }
            if (pkList.size() > 0) {
                createSQL.append("primary key(");
                int i = 0;
                for (String pk : pkList) {
                    createSQL.append(pk);
                    if (i != pkList.size() - 1) {
                        createSQL.append(",");
                    }
                    i++;
                }
                createSQL.append(")");
            }
         	//createSQL.append(")");
            
            // add VectorWise option
            if ("vectorwise_id".equalsIgnoreCase(getDBMSId())){
            	createSQL.append("WITH STRUCTURE = VECTORWISE");
            }
            
            return createSQL.toString();
        }
                
    }
    
    public class HSQLDBManager extends Manager {
    	String [] hsqldbKeyWords = {};
        public HSQLDBManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return HSQLDB;
        }   
        protected String getLProtectedChar() {
            return "";
        }
        protected String getRProtectedChar() {
            return "";
        }     
        protected boolean isHSQLDBKeyword (String keyword) {
            for (int i=0 ; i < hsqldbKeyWords.length ; i++){
                if (hsqldbKeyWords[i].equalsIgnoreCase(keyword)) {
                	return true;
                }
            }
        	return false;
        }
        protected String getLProtectedChar(String keyword) {
        	if (isHSQLDBKeyword(keyword)){
        		return "\\\"";
        	}
        	return getLProtectedChar();
        }
        protected String getRProtectedChar(String keyword) {
        	if (isHSQLDBKeyword(keyword)){
        		return "\\\"";
        	}
        	return getRProtectedChar();
        }  
    }
	
	public class HiveManager extends Manager {
    	String [] hiveKeyWords = {};
        public HiveManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return HIVE;
        }   
        protected String getLProtectedChar() {
            return "";
        }
        protected String getRProtectedChar() {
            return "";
        }     
        protected boolean isHiveKeyword (String keyword) {
            for (int i=0 ; i < hiveKeyWords.length ; i++){
                if (hiveKeyWords[i].equalsIgnoreCase(keyword)) {
                	return true;
                }
            }
        	return false;
        }
        protected String getLProtectedChar(String keyword) {
        	if (isHiveKeyword(keyword)){
        		return "\\\"";
        	}
        	return getLProtectedChar();
        }
        protected String getRProtectedChar(String keyword) {
        	if (isHiveKeyword(keyword)){
        		return "\\\"";
        	}
        	return getRProtectedChar();
        }  
    }
    
    public class InformixManager extends Manager {
        public InformixManager(String cid) {
            super(cid);
        }
        public String getSelectionSQL() {
            StringBuilder selectionSQL = new StringBuilder();
            selectionSQL.append("SELECT COUNT(*) FROM " + getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar());
            return selectionSQL.toString();
        }        
        protected String getDBMSId() {
            return INFORMIX;
        }
        protected String getLProtectedChar() {
            return "";
        }
        protected String getRProtectedChar() {
            return "";
        }        
    }
    
    public class IngresManager extends Manager {
        public IngresManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return INGRES;
        }
        protected String getLProtectedChar() {
            return "\\\"";
        }
        protected String getRProtectedChar() {
            return "\\\"";
        }  
        
    	public String getCopyFromCSVSQL(List<IMetadataColumn> columnList, String fieldDelimiter, 
				String recordDelimiter, String nullIndicator) {		
    		StringBuilder copyBulkSQL = new StringBuilder();
			copyBulkSQL.append("COPY TABLE " + getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar());
			copyBulkSQL.append(" (\"+");	            
			int counter = 0;
			for(IMetadataColumn column : columnList) {
				counter++;
				// add each table field and separator
				copyBulkSQL.append( 
				"\n\"\\n" +
				  column.getOriginalDbColumnName() 
				+ "="
				+ "char(0)");
			
				// add these only if not last entry
				if (counter < columnList.size()){
					copyBulkSQL.append(
					//convertDelim(fieldDelimiter)
					fieldDelimiter
					+ (column.isNullable() ? " WITH NULL ('" + nullIndicator + "')" : "")
					+ " ,"
					+ "\""
					+ "+"
					);
				} else {
					copyBulkSQL.append(
					recordDelimiter
					+ (column.isNullable() ? " WITH NULL ('" + nullIndicator + "')" : "")
					);
					copyBulkSQL.append(")");
				}     	
			} 
			return copyBulkSQL.toString();
		} 

	public String getTruncateTableSQL() {
		StringBuilder truncateTableSQL = new StringBuilder();
		truncateTableSQL.append("MODIFY " + getLProtectedChar() + "\" + tableName_" 
		+ cid + " + \"\\\" TO TRUNCATED");
		return truncateTableSQL.toString();
		}				        
    }
    
    public class VectorWiseManager extends IngresManager {
		public VectorWiseManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return VECTORWISE;
        }
	}	
    
    public class InterbaseManager extends Manager {
        public InterbaseManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return INTERBASE;
        }
        protected String getLProtectedChar() {
            return "\\\"";
        }
        protected String getRProtectedChar() {
            return "\\\"";
        }        
    }
    
    public class JavaDBManager extends Manager {
        public JavaDBManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return JAVADB;
        }
        protected String getLProtectedChar() {
            return "\\\"";
        }
        protected String getRProtectedChar() {
            return "\\\"";
        }        
    }
    
    public class MaxDBManager extends Manager {
        public MaxDBManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return MAXDB;
        }
        protected String getLProtectedChar() {
            return "";
        }
        protected String getRProtectedChar() {
            return "";
        }
    }
    
    public class MSSQLManager extends Manager {
        public MSSQLManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return MSSQL;
        }
        protected String getLProtectedChar() {
            return "[";
        }
        protected String getRProtectedChar() {
            return "]";
        }
        public String getUpdateBulkSQL(List<IMetadataColumn> columnList) {
            StringBuilder updateBulkSQL = new StringBuilder();
            StringBuilder updateSetStmt = new StringBuilder();
            StringBuilder updateWhereStmt = new StringBuilder();
            updateBulkSQL.append("UPDATE " + getLProtectedChar() + "\" +  tableName_" + cid + " + \"" + getRProtectedChar());
            boolean firstKeyColumn = true;
            boolean firstUpdateColumn = true;
            String keySeparator = null;
            String updateSeparator = null;
            for(IMetadataColumn column : columnList) {
                if(column.isKey()) {
                    if(firstKeyColumn) {
                        keySeparator = "";
                        firstKeyColumn = false;
                        updateWhereStmt.append(" FROM " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + " WHERE \" + \"");
                    } else {
                        keySeparator = " AND ";
                    }
                    updateWhereStmt.append(keySeparator);                    
                    updateWhereStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                } else {
                    if(firstUpdateColumn) {
                        updateSeparator = "";
                        firstUpdateColumn = false;
                        updateSetStmt.append(" SET \" + \"");
                    } else {
                        updateSeparator = ", ";
                    }
                    updateSetStmt.append(updateSeparator);
                    updateSetStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                }
            }
            return updateBulkSQL.toString() + updateSetStmt.toString() + updateWhereStmt.toString();            
        } 
       
        protected String getColumnIsNullCondition() {
        	return "0x1";
        }
        
        protected String generateSetBooleanForNullableKeyStmt(Column column, int index, String incomingConnName, String cid,
                int actionType) {
            String prefix = null;
            if (actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if (actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if (actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            setStmt.append(prefix + cid + ".setBoolean(" + index + ", (" + incomingConnName + "." + column.getName()
                    + "==null));\r\n");
            return setStmt.toString();
        } 
        
        public String getCreateTableSQL(List<Column> columnList) {
            MappingTypeRetriever mappingType = MetadataTalendType.getMappingTypeRetriever(getDBMSId());
            StringBuilder createSQL = new StringBuilder();
            createSQL.append("CREATE TABLE " + getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "(");
            List<String> pkList = new ArrayList<String>();
            int count = 0;
            String ending = ",";
            for (Column column : columnList) {
                if (column.isReplaced()) {
                    List<Column> replacedColumns = column.getReplacement();
                    if (column.isKey()) {
                        for (Column replacedColumn : replacedColumns) {
                            pkList.add(getLProtectedChar(replacedColumn.getColumnName()) + replacedColumn.getColumnName() + getRProtectedChar(replacedColumn.getColumnName()));
                        }
                    }
                    int replacedCount = 0;
                    for (Column replacedColumn : replacedColumns) {
                        if (count == columnList.size() - 1 && replacedCount == replacedColumns.size() - 1 && pkList.size() == 0) {
                            ending = "";
                        }
                        createSQL.append(getLProtectedChar(replacedColumn.getColumnName()) + replacedColumn.getColumnName() + getRProtectedChar(replacedColumn.getColumnName()) + " ");
                        createSQL.append(replacedColumn.getDataType() + ending);
                        replacedCount++;
                    }
                } else {
                    if (column.isAddCol()) {
                        if (count == columnList.size() - 1 && pkList.size() == 0) {
                            ending = "";
                        }
                        createSQL.append(getLProtectedChar( column.getColumnName() ) + column.getColumnName() + getRProtectedChar( column.getColumnName() ) + " ");
                        createSQL.append(column.getDataType() + ending);
                    } else {
                        if (column.isKey()) {
                            pkList.add(getLProtectedChar( column.getColumnName() ) + column.getColumnName() + getRProtectedChar( column.getColumnName() ));
                        }
                        createSQL.append(getLProtectedChar( column.getColumnName() ) + column.getColumnName() + getRProtectedChar( column.getColumnName() ) + " ");
                        String dataType = null;
                        if (column.getColumn().getType() == null || column.getColumn().getType().trim().equals("")) {
                            dataType = mappingType.getDefaultSelectedDbType(column.getColumn().getTalendType());
                        } else {
                            dataType = column.getColumn().getType();
                        }
                        if ("mysql_id".equalsIgnoreCase(getDBMSId()) && dataType.endsWith("UNSIGNED")) {                            
                            createSQL.append(dataType.substring(0,dataType.indexOf("UNSIGNED"))) ;                            
                        }else {                            
                            createSQL.append(dataType);
                        }
                        Integer length = column.getColumn().getLength() == null ? 0 : column.getColumn().getLength();
                        Integer precision = column.getColumn().getPrecision() == null ? 0 : column.getColumn().getPrecision();
                        boolean lengthIgnored = mappingType.isLengthIgnored(getDBMSId(), dataType);
                        boolean precisionIgnored = mappingType.isPrecisionIgnored(getDBMSId(), dataType);
                        String prefix = "";
                        String suffix = "";
                        String comma = "";
                        
                        //bug 0016707 fixed:when set INT IDENTITY in schema and use "Specify identity field". the INT IDENTITY in schema by ignored  
                        if(column.isAutoIncrement()) {
                            length = column.getStartValue();
                            precision =  column.getStep();
                            if ("INT IDENTITY".equals(column.column.getType())) {
                                column.setAutoIncrement(false);
                            } 
                        }
                        // end bug0016707
                        
                        if ( ("oracle_id".equalsIgnoreCase(getDBMSId()))
                                && (("NUMBER".equalsIgnoreCase(dataType)) || ("CHAR".equalsIgnoreCase(dataType)) || ("NCHAR".equalsIgnoreCase(dataType)))
                                && (column.getColumn().getLength() == null || 0 == column.getColumn().getLength())
                                && (column.getColumn().getPrecision() == null || 0 == column.getColumn().getPrecision())
                        ){} 
                        else if (("mysql_id".equalsIgnoreCase(getDBMSId()))
                                && (("DECIMAL".equalsIgnoreCase(dataType)) || ("NUMERIC".equalsIgnoreCase(dataType)))
                                && (column.getColumn().getLength() == null || 0 == column.getColumn().getLength())
                                && (column.getColumn().getPrecision() == null || 0 == column.getColumn().getPrecision())
                        ) {}
                        else {
                            if (mappingType.isPreBeforeLength(getDBMSId(), dataType)) {
                                if (!precisionIgnored) {
                                    prefix = "(";
                                    suffix = ") ";
                                    createSQL.append(prefix + precision);
                                }
                                if (!lengthIgnored) {
                                    prefix = (prefix.equals("") ? "(" : prefix);
                                    suffix = (suffix.equals("") ? ") " : suffix);
                                    if (precisionIgnored) {
                                        createSQL.append(prefix);
                                        comma = "";
                                    } else {
                                        comma = ",";
                                    }
                                    createSQL.append(comma + length);
                                }
                                createSQL.append(suffix);
                            } else {
                                if (!lengthIgnored) {
                                    if (("postgres_id".equalsIgnoreCase(getDBMSId()) || "postgresplus_id".equalsIgnoreCase(getDBMSId()) ) && column.getColumn().getLength() == null) {                                    
                                    } else { 
                                        prefix = "(";
                                        suffix = ") ";
                                        createSQL.append(prefix + length);                                    
                                    }
                                }
                                if (!precisionIgnored) {
                                    prefix = (prefix.equals("") ? "(" : prefix);
                                    suffix = (suffix.equals("") ? ") " : suffix);
                                    if (lengthIgnored) {
                                        createSQL.append(prefix);
                                        comma = "";
                                    } else {
                                        comma = ",";
                                    }
                                    createSQL.append(comma + precision);
                                }
                                if (("postgres_id".equalsIgnoreCase(getDBMSId()) || "postgresplus_id".equalsIgnoreCase(getDBMSId()) ) && column.getColumn().getLength() == null) {                                
                                } else {
                                    createSQL.append(suffix);
                                }
                                if("mysql_id".equalsIgnoreCase(getDBMSId()) && dataType.endsWith("UNSIGNED")) {
                                    createSQL.append("UNSIGNED");
                                }
                            }                            
                            
                        }
                        if(column.isAutoIncrement()) {
                            createSQL.append(getAutoIncrement(column.getStartValue(), column.getStep()));
                        } else {
                            createSQL.append(setDefaultValue(column.getColumn().getDefault(), dataType));
                            createSQL.append(setNullable(column.getColumn().isNullable()));
                        }
                        if (count == columnList.size() - 1 && pkList.size() == 0) {
                            ending = "";
                        }
                        createSQL.append(ending);
                    }
                }
                count++;
            }
            if (pkList.size() > 0) {
                createSQL.append("primary key(");
                int i = 0;
                for (String pk : pkList) {
                    createSQL.append(pk);
                    if (i != pkList.size() - 1) {
                        createSQL.append(",");
                    }
                    i++;
                }
                createSQL.append(")");
            }
            //createSQL.append(")");
            
            // add VectorWise option
            //if ("vectorwise_id".equalsIgnoreCase(getDBMSId())){
            //  createSQL.append("WITH STRUCTURE = VECTORWISE");
            //}
            
            return createSQL.toString();
        }
    }
    
    public class MysqlManager extends Manager {
        public MysqlManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return MYSQL;
        }
        protected String getLProtectedChar() {
            return "`";
        }
        protected String getRProtectedChar() {
            return "`";
        }
        public String generateSetStmt(String typeToGenerate, Column column, int index, 
                String incomingConnName, String cid, int actionType) {
            boolean isObject = false;
            String prefix = null;
            if(actionType == NORMAL_TYPE){
                prefix = "pstmt_";
            }else if(actionType == INSERT_TYPE){
                prefix = "pstmtInsert_";
            }else if(actionType == UPDATE_TYPE){
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            if(typeToGenerate.equals("Character")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");                
            } else if(typeToGenerate.equals("Date")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DATE);\r\n");                
            } else if(typeToGenerate.equals("byte[]")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.ARRAY);\r\n");                
            } else if(typeToGenerate.equals("Long") || typeToGenerate.equals("Byte") || typeToGenerate.equals("Integer") || typeToGenerate.equals("Short")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.INTEGER);\r\n");                
            } else if(typeToGenerate.equals("String")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.VARCHAR);\r\n");                
            } else if(typeToGenerate.equals("Object")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.OTHER);\r\n");                
            } else if(typeToGenerate.equals("Boolean")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.BOOLEAN);\r\n");                
            } else if(typeToGenerate.equals("Double")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DOUBLE);\r\n");                
            } else if(typeToGenerate.equals("Float")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.FLOAT);\r\n");                
            }
            if(isObject) {
                setStmt.append("} else {");
            }
            typeToGenerate = getGenerateType(typeToGenerate);
            if(typeToGenerate.equals("Char") || typeToGenerate.equals("Character")) {
                if(isObject) {
                    setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n"); 
                } else {
                    setStmt.append("if(String.valueOf(" + incomingConnName + "." + column.getName() + ").toLowerCase().equals(\"null\")) {\r\n");
                }
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");
                setStmt.append("} else if(" + incomingConnName + "." + column.getName() + " == '\0'){\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", \"\");\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", String.valueOf(" + incomingConnName + "." + column.getName() + "));\r\n");
                setStmt.append("}");
            } else if(typeToGenerate.equals("Date")) {
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " != null) {\r\n");
                setStmt.append("date_" + cid + " = " + incomingConnName + "." + column.getName() + ".getTime();\r\n");
                setStmt.append("if(date_" + cid + " < year1_" + cid + " || date_" + cid + " >= year10000_" + cid + ") {\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", \"0000-00-00 00:00:00\");\r\n");
                setStmt.append("} else {");
                setStmt.append(prefix + cid + ".setTimestamp(" + index + ", new java.sql.Timestamp(date_" + cid + "));\r\n");
                setStmt.append("}\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DATE);\r\n");
                setStmt.append("}\r\n");
            } else {
                setStmt.append(prefix + cid + ".set" + typeToGenerate + "(" + index + ", " + incomingConnName + "." + column.getName() + ");\r\n");
            }
            if(isObject) {
                setStmt.append("}\r\n");
            }
            return setStmt.toString();
        }        
    }

    public class NetezzaManager extends Manager {
        public NetezzaManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return NETEZZA;
        }
        protected String getLProtectedChar() {
            return "";
        }
        protected String getRProtectedChar() {
            return "";
        }
    }
    
    public class OracleManager extends Manager {
    	  private String[] oracleKeyWords= {
    		    	"ACCESS" ,"AUDIT","COMPRESS","DESC" , 
    		    	"ADD","CONNECT","DISTINCT" ,
    		    	"ALL","BY","CREATE","DROP",
    		    	"ALTER","CHAR","CURRENT","ELSE",
    		    	"AND","CHECK","DATE","EXCLUSIVE",
    		    	"ANY","CLUSTER","DECIMAL","	EXISTS",
    		    	"AS","COLUMN","DEFAULT","FILE",
    		    	"ASC","COMMENT","DELETE","FLOAT",
    		    	"FOR","LONG","PCTFREE","SUCCESSFUL",
    		    	"FROM","MAXEXTENTS","PRIOR","SYNONYM",
    		    	"GRANT","MINUS","PRIVILEGES","SYSDATE",
    		    	"GROUP","MODE","PUBLIC","TABLE",
    		    	"HAVING","MODIFY","RAW","THEN",
    		    	"IDENTIFIED","NETWORK","RENAME","TO",
    				"IMMEDIATE","NOAUDIT","RESOURCE","TRIGGER",
    				"IN","NOCOMPRESS","REVOKE","UID",
    				"INCREMENT","NOT","ROW","UNION",
    				"INDEX","NOWAIT","ROWID","UNIQUE",
    				"INITIAL","NULL","ROWNUM","UPDATE",
    				"INSERT","NUMBER","ROWS","USER",
    				"INTEGER","OF","SELECT","VALIDATE",
    				"INTERSECT","OFFLINE","SESSION","VALUES",
    				"INTO","ON","SET","VARCHAR",
    				"IS","ONLINE","SHARE","VARCHAR2",
    				"LEVEL","OPTION","SIZE","VIEW",
    				"LIKE","OR","SMALLINT","WHENEVER",
    				"LOCK","ORDER","START","WHERE","WITH"
    		    	};
        public OracleManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return ORACLE;
        }
        protected String getLProtectedChar() {
            return "";
        }
        protected String getRProtectedChar() {
            return "";
        }   
        protected boolean isOracleKeyword (String keyword) {
            for (int i=0 ; i < oracleKeyWords.length ; i++){
                if (oracleKeyWords[i].equalsIgnoreCase(keyword)) {
                	return true;
                }
            }
        	return false;
        }
        protected boolean contaionsSpaces(String columnName) {
        	if (columnName != null) {
        	    //bug0016837 when use Additional column the coulmn name like: " + "columnNmae" + "
        	    if (columnName.startsWith("\" + ") && columnName.endsWith(" + \"")) {
        	        return false;
        	    }
        	    
        		if (columnName.contains(" ")) {
        			return true;
        		}
        	}
        	return false;
        }
        protected String getLProtectedChar(String keyword) {
        	if (isOracleKeyword(keyword) || contaionsSpaces(keyword)){
        		return "\\\"";
        	}
        	return getLProtectedChar();
        }
        protected String getRProtectedChar(String keyword) {
        	if (isOracleKeyword(keyword) || contaionsSpaces(keyword)){
        		return "\\\"";
        	}
        	return getRProtectedChar();
        }  
        public String getUpdateBulkSQL(List<IMetadataColumn> columnList) {
            StringBuilder updateBulkSQL = new StringBuilder();
            StringBuilder updateSetStmt = new StringBuilder();
            StringBuilder updateSetSelectStmt= new StringBuilder();           
            StringBuilder updateWhereSubStmt = new StringBuilder();
            StringBuilder updateWhereMainStmt = new StringBuilder();
            String keySeparator = null;
            String updateSeparator = null;
            
            boolean firstKeyColumn = true;
            boolean firstUpdateColumn = true;
            updateBulkSQL.append("UPDATE " + getLProtectedChar() + "\" +  tableName_" + cid + " + \"" + getRProtectedChar() );
            for(IMetadataColumn column : columnList) {
                if(column.isKey()) {
                    if(firstKeyColumn) {
                        keySeparator = "";
                        firstKeyColumn = false;
                        updateWhereSubStmt.append(" WHERE \" + \"");
                        updateWhereMainStmt.append(" WHERE EXISTS ( SELECT 1 FROM " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() );
                    }else {
                        keySeparator = " AND ";
                    }
                    updateWhereSubStmt.append(keySeparator);                    
                    updateWhereSubStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());                        

                }else {
                    if(firstUpdateColumn) {
                        updateSeparator = "";
                        firstUpdateColumn = false;
                        updateSetStmt.append(" SET \" + \" ( ");
                        updateSetSelectStmt.append(" = (SELECT ");
                    } else {
                        updateSeparator = ", ";
                    }
                    updateSetStmt.append(updateSeparator);
                    updateSetStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                    updateSetSelectStmt.append(updateSeparator);
                    updateSetSelectStmt.append(getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar()) ;
                }
            }
            updateSetStmt.append(" )");
            updateSetSelectStmt.append(" FROM " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar());
            updateWhereSubStmt.append(")");
            
            return updateBulkSQL.append(updateSetStmt).append(updateSetSelectStmt).append(updateWhereSubStmt).append(updateWhereMainStmt).append(updateWhereSubStmt).toString();
        }        
        
        public String generateSetStmt(String typeToGenerate, String dbType, Column column, int index, String incomingConnName, String cid, int actionType) {
            boolean isObject = false;
            String prefix = null;
            if(actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if(actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if(actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            if(typeToGenerate.equals("Character")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");                
            } else if(typeToGenerate.equals("Date")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DATE);\r\n");                
            } else if(typeToGenerate.equals("byte[]")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                if(dbType != null && (dbType.equals("LONG RAW") || dbType.equals("RAW"))) {
                    setStmt.append(prefix + cid + ".setBytes(" + index + ", null);\r\n");
                } else {
                    setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.ARRAY);\r\n");                    
                }                
            } else if(typeToGenerate.equals("Long") || typeToGenerate.equals("Byte") || typeToGenerate.equals("Integer") || typeToGenerate.equals("Short")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.INTEGER);\r\n");                
            } else if(typeToGenerate.equals("String")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                if(dbType != null && dbType.equals("CLOB")) {
                    setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CLOB);\r\n");                    
                } else {
                    setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.VARCHAR);\r\n");                    
                }                
            } else if(typeToGenerate.equals("Object")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                if(dbType != null && dbType.equals("BLOB")) {
                    setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.BLOB);\r\n");
        		} else if("CLOB".equals(dbType)){
                    setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CLOB);\r\n");
                } else {
                    setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.OTHER);\r\n");                    
                }               
            } else if(typeToGenerate.equals("Boolean")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.INTEGER);\r\n");                
            } else if(typeToGenerate.equals("Double")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DOUBLE);\r\n");                
            } else if(typeToGenerate.equals("Float")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.FLOAT);\r\n");                
            }
            if(isObject) {
                setStmt.append("} else {");
            }
            typeToGenerate = getGenerateType(typeToGenerate);
            if( typeToGenerate.equals("String")&& dbType.toLowerCase().equals("char")) {
                if(isObject) {
                    setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n"); 
                } else {
                    setStmt.append("if(String.valueOf(" + incomingConnName + "." + column.getName() + ").toLowerCase().equals(\"null\")) {\r\n");
                }
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");
                setStmt.append("} else if(" + incomingConnName + "." + column.getName() + " == \"\0\"){\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", \"\");\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", String.valueOf(" + incomingConnName + "." + column.getName() + "));\r\n");
                setStmt.append("}");
            }
            else if(typeToGenerate.equals("Char") || typeToGenerate.equals("Character")) {
                if(isObject) {
                    setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n"); 
                } else {
                    setStmt.append("if(String.valueOf(" + incomingConnName + "." + column.getName() + ").toLowerCase().equals(\"null\")) {\r\n");
                }
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");
                setStmt.append("} else if(" + incomingConnName + "." + column.getName() + " == '\0'){\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", \"\");\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", String.valueOf(" + incomingConnName + "." + column.getName() + "));\r\n");
                setStmt.append("}");
            } else if(typeToGenerate.equals("Date")) {
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " != null) {\r\n");
                if ("Date".equalsIgnoreCase(dbType) && !ElementParameterParser.getValue(node, "__USE_TIMESTAMP_FOR_DATE_TYPE__").equals("true")) {
                    setStmt.append(prefix + cid + ".setDate(" + index + ", new java.sql.Date(" + incomingConnName + "." + column.getName() + ".getTime()));\r\n");
                } else {
                	setStmt.append(prefix + cid + ".setTimestamp(" + index + ", new java.sql.Timestamp(" + incomingConnName + "." + column.getName() + ".getTime()));\r\n");
                }
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DATE);\r\n");
                setStmt.append("}\r\n");
            } else if(typeToGenerate.equals("Bytes") && (dbType != null && (dbType.equals("LONG RAW") || dbType.equals("RAW")))) {
                setStmt.append(prefix + cid + ".setBytes(" + index + ", " + incomingConnName + "." + column.getName() + ");\r\n");
            } else if(typeToGenerate.equals("String") && (dbType != null && dbType.equals("CLOB"))) {
                setStmt.append(prefix + cid + ".setCharacterStream(" + index + ", new java.io.StringReader(" + incomingConnName + "." + column.getName() + "), " + incomingConnName + "." + column.getName() + ".length());\r\n");                
            } else if (typeToGenerate.equals("Bytes") && (dbType != null && dbType.equals("BLOB"))) {
                setStmt.append(prefix + cid + ".setBinaryStream(" + index + ", new java.io.ByteArrayInputStream((byte[])" + incomingConnName + "." + column.getName() + "), ((byte[])" + incomingConnName + "." + column.getName() + ").length);\r\n");
            }else if (typeToGenerate.equals("Object") && (dbType != null && dbType.equals("XMLTYPE"))) {
                setStmt.append("if (" + incomingConnName + "." + column.getName() + " instanceof String) {\r\n");
                setStmt.append(prefix + cid + ".setObject(" + index + ",  xmlTypeUtil_" + cid + ".getCLOB((String)" + incomingConnName + "." + column.getName() + ", conn_" + cid + "));\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".set" + typeToGenerate + "(" + index + ", " + incomingConnName + "." + column.getName() + ");\r\n");
                setStmt.append("}\r\n");
            }
            else {
                setStmt.append(prefix + cid + ".set" + typeToGenerate + "(" + index + ", " + incomingConnName + "." + column.getName() + ");\r\n");
            }
            if(isObject) {
                setStmt.append("}\r\n");
            }
            return setStmt.toString();            
        }
        
        protected String getTableName4Search(boolean useExistingConnection, String connection) {
            return "\""+getLProtectedChar()+ "\" + ((String)" + ElementParameterParser.getValue(node,"__TABLE__") + ").toUpperCase()" +" + \"" + getRProtectedChar() + "\""; 
        }
        
        protected String getUserName4Search(boolean useExistingConnection, String connection) {
            if (useExistingConnection) {
                return "((String)globalMap.get(\"username_" + connection + "\")).toUpperCase()";
            } else {
                return "((String)" + ElementParameterParser.getValue(node,"__USER__") + ").toUpperCase()";
            }    
        }

        protected String getShemaName4Search(boolean useExistingConnection, String connection) {
            return "dbschema_" + cid + ".toUpperCase()"; 
        }   
        
        protected boolean hasSchema() {
            return true;
        }
        
        public void whereStmtSupportNull(StringBuilder updateWhereStmt, Column column) {
            boolean whereSupportNull = false;
			String columnName = column.getColumnName();
			String dataType = column.getDataType();
			if(dataType!=null)
			{
			if("char".equals(dataType.toLowerCase()))
			{
				columnName = "trim("+columnName+")";
			}
			}
            //if node = null, it means some components have not support the "whereNULL" issue yet.
            if (node != null) {
                whereSupportNull = ElementParameterParser.getValue(node, "__SUPPORT_NULL_WHERE__").equals("true");
            }
            if (whereSupportNull && column.getColumn().isNullable()) {
                updateWhereStmt.append("((" + getLProtectedChar(columnName) + columnName + getRProtectedChar(columnName)
                        + " IS NULL AND " + getColumnIsNullCondition() +" " + column.getOperator() + " " + column.getSqlStmt() + ") ");
                updateWhereStmt.append("OR " + getLProtectedChar(columnName) + columnName + getRProtectedChar(columnName)
                        + column.getOperator() + column.getSqlStmt() + ")");
            } else {
                //if node = null, go this branch as the old behave
                updateWhereStmt.append(getLProtectedChar(columnName) + columnName + getRProtectedChar(columnName) + " "
                        + column.getOperator() + " " + column.getSqlStmt());
            }
        }
    }
    
    public class PostgreManager extends Manager {
        public PostgreManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return POSTGRESQL;
        }
        protected String getLProtectedChar() {
            return "\\\"";
        }
        protected String getRProtectedChar() {
            return "\\\"";
        }
        public String generateSetStmt(String typeToGenerate, Column column, int index, 
                String incomingConnName, String cid, int actionType) {
            boolean isObject = false;
            String prefix = null;
            if(actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if(actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if(actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            if(typeToGenerate.equals("Character")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");                
            } else if(typeToGenerate.equals("Date")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.TIMESTAMP);\r\n");                
            } else if(typeToGenerate.equals("byte[]")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.ARRAY);\r\n");                
            } else if(typeToGenerate.equals("Long") || typeToGenerate.equals("Byte") || typeToGenerate.equals("Integer") || typeToGenerate.equals("Short")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.INTEGER);\r\n");                
            } else if(typeToGenerate.equals("String")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.VARCHAR);\r\n");                
            } else if(typeToGenerate.equals("Object")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.OTHER);\r\n");                
            } else if(typeToGenerate.equals("Boolean")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.BOOLEAN);\r\n");                
            } else if(typeToGenerate.equals("Double")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DOUBLE);\r\n");                
            } else if(typeToGenerate.equals("Float")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.FLOAT);\r\n");                
            }
            if(isObject) {
                setStmt.append("} else {");
            }
            typeToGenerate = getGenerateType(typeToGenerate);
            if(typeToGenerate.equals("Char") || typeToGenerate.equals("Character")) {
                if(isObject) {
                    setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n"); 
                } else {
                    setStmt.append("if(String.valueOf(" + incomingConnName + "." + column.getName() + ").toLowerCase().equals(\"null\")) {\r\n");
                }
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");
                setStmt.append("} else if(" + incomingConnName + "." + column.getName() + " == '\0'){\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", \"\");\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", String.valueOf(" + incomingConnName + "." + column.getName() + "));\r\n");
                setStmt.append("}");
            } else if(typeToGenerate.equals("Date")) {
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " != null) {\r\n");
                setStmt.append(prefix + cid + ".setTimestamp(" + index + ", new java.sql.Timestamp(" + incomingConnName + "." + column.getName() + ".getTime()));\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.TIMESTAMP);\r\n");
                setStmt.append("}\r\n");
			} else if(typeToGenerate.equals("Geometry")) {
            	setStmt.append("if(" + incomingConnName + "." + column.getName() + " != null) {\r\n");
                // Load geometry
                setStmt.append(
                                prefix +
                                cid +
                                ".setString(" + 
                                    index +
                                    ", ((org.talend.sdi.geometry.Geometry)(" +
                                    incomingConnName + "." + column.getName() + ")).toString()" +
                                    ");\r\n");
                // TODO : should use forceCoordinate first, then getSRID method or -1 if it's null.
                setStmt.append(
                                prefix +
                                cid +
                                ".setInt(" + 
                                    index + 
                                    " + 1, ((org.talend.sdi.geometry.Geometry)(" + 
                                    incomingConnName + "." + column.getName() + ")).getSRID()" +
                                    ");\r\n");
				setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.VARCHAR);\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + " + 1 , java.sql.Types.INTEGER);\r\n");
                setStmt.append("}\r\n");    
            } else {
                setStmt.append(prefix + cid + ".set" + typeToGenerate + "(" + index + ", " + incomingConnName + "." + column.getName() + ");\r\n");
            }
            if(isObject) {
                setStmt.append("}\r\n");
            }
            return setStmt.toString();
        }
        public String getUpdateBulkSQL(List<IMetadataColumn> columnList) {
            StringBuilder updateBulkSQL = new StringBuilder();
            StringBuilder updateSetStmt = new StringBuilder();
            StringBuilder updateWhereStmt = new StringBuilder();
            updateBulkSQL.append("UPDATE " + getLProtectedChar() + "\" +  tableName_" + cid + " + \"" + getRProtectedChar());
            boolean firstKeyColumn = true;
            boolean firstUpdateColumn = true;
            String keySeparator = null;
            String updateSeparator = null;
            for(IMetadataColumn column : columnList) {
                if(column.isKey()) {
                    if(firstKeyColumn) {
                        keySeparator = "";
                        firstKeyColumn = false;
                        updateWhereStmt.append(" FROM " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + " WHERE \" + \"");
                    } else {
                        keySeparator = " AND ";
                    }
                    updateWhereStmt.append(keySeparator);                    
                    updateWhereStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                } else {
                    if(firstUpdateColumn) {
                        updateSeparator = "";
                        firstUpdateColumn = false;
                        updateSetStmt.append(" SET \" + \"");
                    } else {
                        updateSeparator = ", ";
                    }
                    updateSetStmt.append(updateSeparator);
                    updateSetStmt.append(getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                }
            }
            return updateBulkSQL.toString() + updateSetStmt.toString() + updateWhereStmt.toString();            
        }        
    }
    
    public class PostgrePlusManager extends Manager {
        public PostgrePlusManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return POSTGREPLUS;
        }
        protected String getLProtectedChar() {
            return "\\\"";
        }
        protected String getRProtectedChar() {
            return "\\\"";
        }
        
        public String generateSetStmt(String typeToGenerate, Column column, int index, 
                String incomingConnName, String cid, int actionType) {
            boolean isObject = false;
            String prefix = null;
            if(actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if(actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if(actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            if(typeToGenerate.equals("Character")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");                
            } else if(typeToGenerate.equals("Date")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.TIMESTAMP);\r\n");                
            } else if(typeToGenerate.equals("byte[]")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.ARRAY);\r\n");                
            } else if(typeToGenerate.equals("Long") || typeToGenerate.equals("Byte") || typeToGenerate.equals("Integer") || typeToGenerate.equals("Short")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.INTEGER);\r\n");                
            } else if(typeToGenerate.equals("String")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.VARCHAR);\r\n");                
            } else if(typeToGenerate.equals("Object")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.OTHER);\r\n");                
            } else if(typeToGenerate.equals("Boolean")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.BOOLEAN);\r\n");                
            } else if(typeToGenerate.equals("Double")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DOUBLE);\r\n");                
            } else if(typeToGenerate.equals("Float")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.FLOAT);\r\n");                
            }
            if(isObject) {
                setStmt.append("} else {");
            }
            typeToGenerate = getGenerateType(typeToGenerate);
            if(typeToGenerate.equals("Char") || typeToGenerate.equals("Character")) {
                if(isObject) {
                    setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n"); 
                } else {
                    setStmt.append("if(String.valueOf(" + incomingConnName + "." + column.getName() + ").toLowerCase().equals(\"null\")) {\r\n");
                }
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");
                setStmt.append("} else if(" + incomingConnName + "." + column.getName() + " == '\0'){\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", \"\");\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", String.valueOf(" + incomingConnName + "." + column.getName() + "));\r\n");
                setStmt.append("}");
            } else if(typeToGenerate.equals("Date")) {
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " != null) {\r\n");
                setStmt.append(prefix + cid + ".setTimestamp(" + index + ", new java.sql.Timestamp(" + incomingConnName + "." + column.getName() + ".getTime()));\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.TIMESTAMP);\r\n");
                setStmt.append("}\r\n");
			} else if(typeToGenerate.equals("Geometry")) {
            	setStmt.append("if(" + incomingConnName + "." + column.getName() + " != null) {\r\n");
                // Load geometry
                setStmt.append(
                                prefix +
                                cid +
                                ".setString(" + 
                                    index +
                                    ", ((org.talend.sdi.geometry.Geometry)(" +
                                    incomingConnName + "." + column.getName() + ")).toString()" +
                                    ");\r\n");
                // TODO : should use forceCoordinate first, then getSRID method or -1 if it's null.
                setStmt.append(
                                prefix +
                                cid +
                                ".setInt(" + 
                                    index + 
                                    " + 1, ((org.talend.sdi.geometry.Geometry)(" + 
                                    incomingConnName + "." + column.getName() + ")).getSRID()" +
                                    ");\r\n");
				setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.VARCHAR);\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + " + 1 , java.sql.Types.INTEGER);\r\n");
                setStmt.append("}\r\n");    
            } else {
                setStmt.append(prefix + cid + ".set" + typeToGenerate + "(" + index + ", " + incomingConnName + "." + column.getName() + ");\r\n");
            }
            if(isObject) {
                setStmt.append("}\r\n");
            }
            return setStmt.toString();
        }
        
        public String getUpdateBulkSQL(List<IMetadataColumn> columnList) {
            StringBuilder updateBulkSQL = new StringBuilder();
            StringBuilder updateSetStmt = new StringBuilder();
            StringBuilder updateWhereStmt = new StringBuilder();
            updateBulkSQL.append("UPDATE " + getLProtectedChar() + "\" +  tableName_" + cid + " + \"" + getRProtectedChar());
            boolean firstKeyColumn = true;
            boolean firstUpdateColumn = true;
            String keySeparator = null;
            String updateSeparator = null;
            for(IMetadataColumn column : columnList) {
                if(column.isKey()) {
                    if(firstKeyColumn) {
                        keySeparator = "";
                        firstKeyColumn = false;
                        updateWhereStmt.append(" FROM " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + " WHERE \" + \"");
                    } else {
                        keySeparator = " AND ";
                    }
                    updateWhereStmt.append(keySeparator);                    
                    updateWhereStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                } else {
                    if(firstUpdateColumn) {
                        updateSeparator = "";
                        firstUpdateColumn = false;
                        updateSetStmt.append(" SET \" + \"");
                    } else {
                        updateSeparator = ", ";
                    }
                    updateSetStmt.append(updateSeparator);
                    updateSetStmt.append(getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                }
            }
            return updateBulkSQL.toString() + updateSetStmt.toString() + updateWhereStmt.toString();            
        }        
    }
    
    public class SQLiteManager extends Manager {
        public SQLiteManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return SQLITE;
        }
        protected String getLProtectedChar() {
            return "\\\"";
        }
        protected String getRProtectedChar() {
            return "\\\"";
        }        
    }
    
    public class SybaseManager extends Manager {
    	 private String[] sybaseKeyWords= {
    			 "ACCESS" ,"AUDIT","COMPRESS","DESC" , 
 		    	"ADD","CONNECT","COUNT","DISTINCT" ,
 		    	"ALL","BY","CREATE","DROP",
 		    	"ALTER","CHAR","CURRENT","ELSE",
 		    	"AND","CHECK","DATE","EXCLUSIVE",
 		    	"ANY","CLUSTER","DECIMAL","	EXISTS",
 		    	"AS","COLUMN","DEFAULT","FILE",
 		    	"ASC","COMMENT","DELETE","FLOAT",
 		    	"FOR","LONG","PCTFREE","SUCCESSFUL",
 		    	"FROM","FALSE","MAXEXTENTS","PRIOR","SYNONYM",
 		    	"GRANT","MINUS","PRIVILEGES","SYSDATE",
 		    	"GROUP","MODE","PUBLIC","TABLE",
 		    	"HAVING","MODIFY","RAW","THEN",
 		    	"IDENTIFIED","NETWORK","RENAME","TO",
 				"IMMEDIATE","NOAUDIT","RESOURCE","TRIGGER",
 				"IN","NOCOMPRESS","REVOKE","UID",
 				"INCREMENT","NOT","ROW","UNION",
 				"INDEX","NOWAIT","ROWID","UNIQUE",
 				"INITIAL","NULL","ROWNUM","UPDATE",
 				"INSERT","NUMBER","ROWS","USER",
 				"INTEGER","OF","SELECT","VALIDATE",
 				"INTERSECT","OFFLINE","SESSION","VALUES",
 				"INTO","ON","SET","VARCHAR",
 				"IS","ONLINE","SHARE",
 				"LEVEL","OPTION","SIZE","VIEW",
 				"LIKE","OR","SMALLINT","WHENEVER",
 				"LOCK","ORDER","START","WHERE","WITH"
    			 
    	 };
        public SybaseManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return SYBASE;
        }
        protected String getLProtectedChar() {
            return "";
        }
        protected String getRProtectedChar() {
            return "";
        }
        protected boolean isSybaseKeyword (String keyword) {
            for (int i=0 ; i < sybaseKeyWords.length ; i++){
                if (sybaseKeyWords[i].equalsIgnoreCase(keyword)) {
                	return true;
                }
            }
        	return false;
        }
        protected String getLProtectedChar(String keyword) {
        	if (isSybaseKeyword(keyword)){
        		return "\\\"";
        	}
        	return getLProtectedChar();
        }
        protected String getRProtectedChar(String keyword) {
        	if (isSybaseKeyword(keyword)){
        		return "\\\"";
        	}
        	return getRProtectedChar();
        }  
        protected String setNullable(boolean nullable) {
            if(!nullable) {
                return " not null ";
            } else {
                return " null ";
            }
        }        
        public String getUpdateBulkSQL(List<IMetadataColumn> columnList) {
            StringBuilder updateBulkSQL = new StringBuilder();
            StringBuilder updateSetStmt = new StringBuilder();
            StringBuilder updateWhereStmt = new StringBuilder();
            updateBulkSQL.append("UPDATE " + getLProtectedChar() + "\" +  tableName_" + cid + " + \"" + getRProtectedChar());
            boolean firstKeyColumn = true;
            boolean firstUpdateColumn = true;
            String keySeparator = null;
            String updateSeparator = null;
            for(IMetadataColumn column : columnList) {
                if(column.isKey()) {
                    if(firstKeyColumn) {
                        keySeparator = "";
                        firstKeyColumn = false;
                        updateWhereStmt.append(" FROM " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + " WHERE \" + \"");
                    } else {
                        keySeparator = " AND ";
                    }
                    updateWhereStmt.append(keySeparator);                    
                    updateWhereStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                } else {
                    if(firstUpdateColumn) {
                        updateSeparator = "";
                        firstUpdateColumn = false;
                        updateSetStmt.append(" SET \" + \"");
                    } else {
                        updateSeparator = ", ";
                    }
                    updateSetStmt.append(updateSeparator);
                    updateSetStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                }
            }
            return updateBulkSQL.toString() + updateSetStmt.toString() + updateWhereStmt.toString();            
        }        
    }
    
    public class TeradataManager extends Manager {
        public TeradataManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return TERADATA;
        }
        protected String getLProtectedChar() {
            return "\\\"";
        }
        protected String getRProtectedChar() {
            return "\\\"";
        } 
                
        public String generateSetStmt(String typeToGenerate, Column column, int index, 
                String incomingConnName, String cid, int actionType) {
            boolean isObject = false;
            String prefix = null;
            if(actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if(actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if(actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            if(typeToGenerate.equals("Character")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");                
            } else if(typeToGenerate.equals("Date")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.TIMESTAMP);\r\n");                
            } else if(typeToGenerate.equals("byte[]")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.ARRAY);\r\n");                
            } else if(typeToGenerate.equals("Long") || typeToGenerate.equals("Byte") || typeToGenerate.equals("Integer") || typeToGenerate.equals("Short")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.INTEGER);\r\n");                
            } else if(typeToGenerate.equals("String")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.VARCHAR);\r\n");                
            } else if(typeToGenerate.equals("Object")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.OTHER);\r\n");                
            } else if(typeToGenerate.equals("Boolean")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.BOOLEAN);\r\n");                
            } else if(typeToGenerate.equals("Double")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DOUBLE);\r\n");                
            } else if(typeToGenerate.equals("Float")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.FLOAT);\r\n");                
            }
            if(isObject) {
                setStmt.append("} else {");
            }
            typeToGenerate = getGenerateType(typeToGenerate);
            if(typeToGenerate.equals("Char") || typeToGenerate.equals("Character")) {
                if(isObject) {
                    setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n"); 
                } else {
                    setStmt.append("if(String.valueOf(" + incomingConnName + "." + column.getName() + ").toLowerCase().equals(\"null\")) {\r\n");
                }
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");
                setStmt.append("} else if(" + incomingConnName + "." + column.getName() + " == '\0'){\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", \"\");\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", String.valueOf(" + incomingConnName + "." + column.getName() + "));\r\n");
                setStmt.append("}");
            } else if(typeToGenerate.equals("Date")) {
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " != null) {\r\n");
                setStmt.append(prefix + cid + ".setTimestamp(" + index + ", new java.sql.Timestamp(" + incomingConnName + "." + column.getName() + ".getTime()));\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.TIMESTAMP);\r\n");
                setStmt.append("}\r\n");
			} else if(typeToGenerate.equals("Geometry")) {
            	setStmt.append("if(" + incomingConnName + "." + column.getName() + " != null) {\r\n");
                // Load geometry
                setStmt.append(
                                prefix +
                                cid +
                                ".setString(" + 
                                    index +
                                    ", ((org.talend.sdi.geometry.Geometry)(" +
                                    incomingConnName + "." + column.getName() + ")).toString()" +
                                    ");\r\n");
                // TODO : should use forceCoordinate first, then getSRID method or -1 if it's null.
                setStmt.append(
                                prefix +
                                cid +
                                ".setInt(" + 
                                    index + 
                                    " + 1, ((org.talend.sdi.geometry.Geometry)(" + 
                                    incomingConnName + "." + column.getName() + ")).getSRID()" +
                                    ");\r\n");
				setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.VARCHAR);\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + " + 1 , java.sql.Types.INTEGER);\r\n");
                setStmt.append("}\r\n");    
            } else {
                setStmt.append(prefix + cid + ".set" + typeToGenerate + "(" + index + ", " + incomingConnName + "." + column.getName() + ");\r\n");
            }
            if(isObject) {
                setStmt.append("}\r\n");
            }
            return setStmt.toString();
        }
        public String getCreateTableSQL(List<Column> columnList) {
            MappingTypeRetriever mappingType = MetadataTalendType.getMappingTypeRetriever(getDBMSId());
            StringBuilder createSQL = new StringBuilder();
            
            String tableType = ElementParameterParser.getValue(node, "__CREATE_TABLE_TYPE__");
            
            createSQL.append("CREATE " + tableType + " TABLE " + getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "(");
            List<String> pkList = new ArrayList<String>();
            int count = 0;
            String ending = ",";
            for (Column column : columnList) {
                if (column.isReplaced()) {
                    List<Column> replacedColumns = column.getReplacement();
                    if (column.isKey()) {
                        for (Column replacedColumn : replacedColumns) {
                            pkList.add(getLProtectedChar(replacedColumn.getColumnName()) + replacedColumn.getColumnName() + getRProtectedChar(replacedColumn.getColumnName()));
                        }
                    }
                    int replacedCount = 0;
                    for (Column replacedColumn : replacedColumns) {
                        if (count == columnList.size() - 1 && replacedCount == replacedColumns.size() - 1 && pkList.size() == 0) {
                            ending = "";
                        }
                        createSQL.append(getLProtectedChar(replacedColumn.getColumnName()) + replacedColumn.getColumnName() + getRProtectedChar(replacedColumn.getColumnName()) + " ");
                        createSQL.append(replacedColumn.getDataType() + ending);
                        replacedCount++;
                    }
                } else {
                    if (column.isAddCol()) {
                        if (count == columnList.size() - 1 && pkList.size() == 0) {
                            ending = "";
                        }
                        createSQL.append(getLProtectedChar( column.getColumnName() ) + column.getColumnName() + getRProtectedChar( column.getColumnName() ) + " ");
                        createSQL.append(column.getDataType() + ending);
                    } else {
                        if (column.isKey()) {
                            pkList.add(getLProtectedChar( column.getColumnName() ) + column.getColumnName() + getRProtectedChar( column.getColumnName() ));
                        }
                        createSQL.append(getLProtectedChar( column.getColumnName() ) + column.getColumnName() + getRProtectedChar( column.getColumnName() ) + " ");
                        String dataType = null;
                        if (column.getColumn().getType() == null || column.getColumn().getType().trim().equals("")) {
                            dataType = mappingType.getDefaultSelectedDbType(column.getColumn().getTalendType());
                        } else {
                            dataType = column.getColumn().getType();
                        }
                        if ("mysql_id".equalsIgnoreCase(getDBMSId()) && dataType.endsWith("UNSIGNED")) {                            
                            createSQL.append(dataType.substring(0,dataType.indexOf("UNSIGNED"))) ;                            
                        }else {                            
                            createSQL.append(dataType);
                        }
                        Integer length = column.getColumn().getLength() == null ? 0 : column.getColumn().getLength();
                        Integer precision = column.getColumn().getPrecision() == null ? 0 : column.getColumn().getPrecision();
                        boolean lengthIgnored = mappingType.isLengthIgnored(getDBMSId(), dataType);
                        boolean precisionIgnored = mappingType.isPrecisionIgnored(getDBMSId(), dataType);
                        String prefix = "";
                        String suffix = "";
                        String comma = "";
                        
                        if ( ("oracle_id".equalsIgnoreCase(getDBMSId()))
                                && (("NUMBER".equalsIgnoreCase(dataType)) || ("CHAR".equalsIgnoreCase(dataType)) || ("NCHAR".equalsIgnoreCase(dataType)))
                                && (column.getColumn().getLength() == null || 0 == column.getColumn().getLength())
                                && (column.getColumn().getPrecision() == null || 0 == column.getColumn().getPrecision())
                        ){} 
                        else if (("mysql_id".equalsIgnoreCase(getDBMSId()))
                                && (("DECIMAL".equalsIgnoreCase(dataType)) || ("NUMERIC".equalsIgnoreCase(dataType)))
                                && (column.getColumn().getLength() == null || 0 == column.getColumn().getLength())
                                && (column.getColumn().getPrecision() == null || 0 == column.getColumn().getPrecision())
                        ) {}
                        else {
                            if (mappingType.isPreBeforeLength(getDBMSId(), dataType)) {
                                if (!precisionIgnored) {
                                    prefix = "(";
                                    suffix = ") ";
                                    createSQL.append(prefix + precision);
                                }
                                if (!lengthIgnored) {
                                    prefix = (prefix.equals("") ? "(" : prefix);
                                    suffix = (suffix.equals("") ? ") " : suffix);
                                    if (precisionIgnored) {
                                        createSQL.append(prefix);
                                        comma = "";
                                    } else {
                                        comma = ",";
                                    }
                                    createSQL.append(comma + length);
                                }
                                createSQL.append(suffix);
                            } else {
                                if (!lengthIgnored) {
                                    if (("postgres_id".equalsIgnoreCase(getDBMSId()) || "postgresplus_id".equalsIgnoreCase(getDBMSId()) ) && column.getColumn().getLength() == null) {                                    
                                    } else { 
                                        prefix = "(";
                                        suffix = ") ";
                                        createSQL.append(prefix + length);                                    
                                    }
                                }
                                if (!precisionIgnored) {
                                    prefix = (prefix.equals("") ? "(" : prefix);
                                    suffix = (suffix.equals("") ? ") " : suffix);
                                    if (lengthIgnored) {
                                        createSQL.append(prefix);
                                        comma = "";
                                    } else {
                                        comma = ",";
                                    }
                                    createSQL.append(comma + precision);
                                }
                                if (("postgres_id".equalsIgnoreCase(getDBMSId()) || "postgresplus_id".equalsIgnoreCase(getDBMSId()) ) && column.getColumn().getLength() == null) {                                
                                } else {
                                    createSQL.append(suffix);
                                }
                                if("mysql_id".equalsIgnoreCase(getDBMSId()) && dataType.endsWith("UNSIGNED")) {
                                    createSQL.append("UNSIGNED");
                                }
                            }                            
                            
                        }
                        if(column.isAutoIncrement()) {
                            createSQL.append(getAutoIncrement(column.getStartValue(), column.getStep()));
                        } else {
                            createSQL.append(setDefaultValue(column.getColumn().getDefault(), dataType));
                            createSQL.append(setNullable(column.getColumn().isNullable()));
                        }
                        if (count == columnList.size() - 1 && pkList.size() == 0) {
                            ending = "";
                        }
                        createSQL.append(ending);
                    }
                }
                count++;
            }
            if (pkList.size() > 0) {
                createSQL.append("primary key(");
                int i = 0;
                for (String pk : pkList) {
                    createSQL.append(pk);
                    if (i != pkList.size() - 1) {
                        createSQL.append(",");
                    }
                    i++;
                }
                createSQL.append(")");
            }
            //createSQL.append(")");
            return createSQL.toString();
        }
    }
    
    public class VerticaManager extends Manager {
        public VerticaManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return VERTICA;
        }
        protected String getLProtectedChar() {
            return "";
        }
        protected String getRProtectedChar() {
            return "";
        }
        public String getUpdateBulkSQL(List<IMetadataColumn> columnList) {
            StringBuilder updateBulkSQL = new StringBuilder();
            StringBuilder updateSetStmt = new StringBuilder();
            StringBuilder updateWhereStmt = new StringBuilder();
            updateBulkSQL.append("UPDATE " + getLProtectedChar() + "\" +  tableName_" + cid + " + \"" + getRProtectedChar());
            boolean firstKeyColumn = true;
            boolean firstUpdateColumn = true;
            String keySeparator = null;
            String updateSeparator = null;
            for(IMetadataColumn column : columnList) {
                if(column.isKey()) {
                    if(firstKeyColumn) {
                        keySeparator = "";
                        firstKeyColumn = false;
                        updateWhereStmt.append(" FROM " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + " WHERE \" + \"");
                    } else {
                        keySeparator = " AND ";
                    }
                    updateWhereStmt.append(keySeparator);                    
                    updateWhereStmt.append(getLProtectedChar() + "\" + tableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                } else {
                    if(firstUpdateColumn) {
                        updateSeparator = "";
                        firstUpdateColumn = false;
                        updateSetStmt.append(" SET \" + \"");
                    } else {
                        updateSeparator = ", ";
                    }
                    updateSetStmt.append(updateSeparator);
                    updateSetStmt.append(getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar() + " = " + getLProtectedChar() + "\" + tmpTableName_" + cid + " + \"" + getRProtectedChar() + "." + getLProtectedChar() + "\" + \"" + column.getLabel() + "\" + \"" + getRProtectedChar());
                }
            }
            return updateBulkSQL.toString() + updateSetStmt.toString() + updateWhereStmt.toString();            
        }
        public String generateSetStmt(String typeToGenerate, Column column, int index, 
                String incomingConnName, String cid, int actionType) {
            boolean isObject = false;
            String prefix = null;
            if(actionType == NORMAL_TYPE) {
                prefix = "pstmt_";
            } else if(actionType == INSERT_TYPE) {
                prefix = "pstmtInsert_";
            } else if(actionType == UPDATE_TYPE) {
                prefix = "pstmtUpdate_";
            }
            StringBuilder setStmt = new StringBuilder();
            if(typeToGenerate.equals("Character")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");                
            } else if(typeToGenerate.equals("Date")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DATE);\r\n");                
            } else if(typeToGenerate.equals("byte[]")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.ARRAY);\r\n");                
            } else if(typeToGenerate.equals("Long") || typeToGenerate.equals("Byte") || typeToGenerate.equals("Integer") || typeToGenerate.equals("Short")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.INTEGER);\r\n");                
            } else if(typeToGenerate.equals("String")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.VARCHAR);\r\n");                
            } else if(typeToGenerate.equals("Object")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.OTHER);\r\n");                
            } else if(typeToGenerate.equals("Boolean")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.BOOLEAN);\r\n");                
            } else if(typeToGenerate.equals("Double")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DOUBLE);\r\n");                
            } else if(typeToGenerate.equals("Float")) {
                isObject = true;
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.FLOAT);\r\n");                
            }
            if(isObject) {
                setStmt.append("} else {");
            }
            typeToGenerate = getGenerateType(typeToGenerate);
            if(typeToGenerate.equals("Char") || typeToGenerate.equals("Character")) {
                if(isObject) {
                    setStmt.append("if(" + incomingConnName + "." + column.getName() + " == null) {\r\n"); 
                } else {
                    setStmt.append("if(String.valueOf(" + incomingConnName + "." + column.getName() + ").toLowerCase().equals(\"null\")) {\r\n");
                }
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.CHAR);\r\n");
                setStmt.append("} else if(" + incomingConnName + "." + column.getName() + " == '\0'){\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", \"\");\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setString(" + index + ", String.valueOf(" + incomingConnName + "." + column.getName() + "));\r\n");
                setStmt.append("}");
            } else if(typeToGenerate.equals("Date")) {
                setStmt.append("if(" + incomingConnName + "." + column.getName() + " != null) {\r\n");
                setStmt.append(prefix + cid + ".setTimestamp(" + index + ", new java.sql.Timestamp(" + incomingConnName + "." + column.getName() + ".getTime()));\r\n");
                setStmt.append("} else {\r\n");
                setStmt.append(prefix + cid + ".setNull(" + index + ", java.sql.Types.DATE);\r\n");
                setStmt.append("}\r\n");
            } else {
                if(typeToGenerate.equals("Int")) {
                    setStmt.append(prefix + cid + ".setLong(" + index + ", " + incomingConnName + "." + column.getName() + ");\r\n");
                } else {
                    setStmt.append(prefix + cid + ".set" + typeToGenerate + "(" + index + ", " + incomingConnName + "." + column.getName() + ");\r\n");                 
                }
            }
            if(isObject) {
                setStmt.append("}\r\n");
            }
            return setStmt.toString();
        }        
    }
    
    public class ODBCManager extends Manager {
        public ODBCManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return ODBC;
        }
        protected String getLProtectedChar() {
            return "";
        }
        protected String getRProtectedChar() {
            return "";
        }        
    }
    
    public class JDBCManager extends Manager {
        public JDBCManager(String cid) {
            super(cid);
        }
        protected String getDBMSId() {
            return JDBC;
        }
        protected String getLProtectedChar() {
            return "";
        }
        protected String getRProtectedChar() {
            return "";
        }        
    }    
    
    public Manager getManager(String dbmsId, String cid) {
        Manager manager = managerMap.get(dbmsId + cid);
        if(manager == null) {
            if(dbmsId.equalsIgnoreCase(AS400)) {
                manager = new AS400Manager(cid);
            } else if(dbmsId.equalsIgnoreCase(ACCESS)) {
                manager = new AccessManager(cid);
            } else if(dbmsId.equalsIgnoreCase(DB2)) {
                manager = new DB2Manager(cid);
            } else if(dbmsId.equalsIgnoreCase(FIREBIRD)) {
                manager = new FirebirdManager(cid);
            } else if(dbmsId.equalsIgnoreCase(HSQLDB)) {
                manager = new HSQLDBManager(cid);
            } else if(dbmsId.equalsIgnoreCase(HIVE)) {
                manager = new HiveManager(cid);
            } else if(dbmsId.equalsIgnoreCase(INFORMIX)) {
                manager = new InformixManager(cid);
            } else if(dbmsId.equalsIgnoreCase(INGRES)) {
                manager = new IngresManager(cid);         
            } else if(dbmsId.equalsIgnoreCase(VECTORWISE)) {
                manager = new VectorWiseManager(cid);
            } else if(dbmsId.equalsIgnoreCase(INTERBASE)) {
                manager = new InterbaseManager(cid);
            } else if(dbmsId.equalsIgnoreCase(JAVADB)) {
                manager = new JavaDBManager(cid);
            } else if(dbmsId.equalsIgnoreCase(MAXDB)) {
                manager = new MaxDBManager(cid);
            } else if(dbmsId.equalsIgnoreCase(MSSQL)) {
                manager = new MSSQLManager(cid);
            } else if(dbmsId.equalsIgnoreCase(MYSQL)) {
                manager = new MysqlManager(cid);
            } else if(dbmsId.equalsIgnoreCase(NETEZZA)) {
                manager = new NetezzaManager(cid);
            } else if(dbmsId.equalsIgnoreCase(ORACLE)) {
                manager = new OracleManager(cid);
            } else if(dbmsId.equalsIgnoreCase(POSTGREPLUS)) {
                manager = new PostgrePlusManager(cid);
            } else if(dbmsId.equalsIgnoreCase(POSTGRESQL)) {
                manager = new PostgreManager(cid);
            } else if(dbmsId.equalsIgnoreCase(SQLITE)) {
                manager = new SQLiteManager(cid);
            } else if(dbmsId.equalsIgnoreCase(SYBASE)) {
                manager = new SybaseManager(cid);
            } else if(dbmsId.equalsIgnoreCase(TERADATA)) {
                manager = new TeradataManager(cid);
            } else if(dbmsId.equalsIgnoreCase(VERTICA)) {
                manager = new VerticaManager(cid);
            } else if(dbmsId.equalsIgnoreCase(ODBC)) {
                manager = new ODBCManager(cid);
            } else if(dbmsId.equalsIgnoreCase(JDBC)) {
                manager = new JDBCManager(cid);
            }
        }
        managerMap.put(dbmsId + cid, manager);
        return manager;
    }
    
    
    public Manager getManager(String dbmsId, String cid, INode node) {
        Manager manager = getManager(dbmsId, cid);
        manager.setNode(node);
        return manager;
    }    
    
    public List<IMetadataColumn> getColumnList(INode node) {
        List<IMetadataColumn> columnList = null;
        List<IMetadataTable> metadatas = node.getMetadataList();
        if(metadatas != null && metadatas.size() > 0) {
            IMetadataTable metadata = metadatas.get(0);
            if(metadata != null) {
                columnList = metadata.getListColumns();
            }
        }
        return columnList;
    }

    public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();	
	String cid = node.getUniqueName();	
	String dataAction = ElementParameterParser.getValue(node,"__DATA_ACTION__");
	String dbmsId = ElementParameterParser.getValue(node,"__MAPPING__");
	String tableName = ElementParameterParser.getValue(node,"__TABLE__");
    String dieOnError = ElementParameterParser.getValue(node, "__DIE_ON_ERROR__");
	String table = ElementParameterParser.getValue(node,"__TABLE__");	
	String tableAction = ElementParameterParser.getValue(node,"__TABLE_ACTION__");	
	List<Map<String, String>> addCols =
            (List<Map<String,String>>)ElementParameterParser.getObjectValue(node,"__ADD_COLS__");

    boolean useFieldOptions = ("true").equals(ElementParameterParser.getValue(node, "__USE_FIELD_OPTIONS__"));
    
    List<Map<String, String>> fieldOptions = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__FIELD_OPTIONS__");	
	
    String commitEvery = ElementParameterParser.getValue(node, "__COMMIT_EVERY__");	
    
    boolean useExistingConnection = "true".equals(ElementParameterParser.getValue(node,"__USE_EXISTING_CONNECTION__"));
    
    boolean isEnableDebug = ("true").equals(ElementParameterParser.getValue(node,"__ENABLE_DEBUG_MODE__"));
	
	boolean useBatchSize = ("true").equals(ElementParameterParser.getValue(node,"__USE_BATCH_SIZE__"));
	String batchSize=ElementParameterParser.getValue(node,"__BATCH_SIZE__");
	
	//feature:2880
    getManager(dbmsId, cid, node);
    boolean whereSupportNull = ElementParameterParser.getValue(node, "__SUPPORT_NULL_WHERE__").equals("true");
	
	String incomingConnName = null;
	List<IMetadataColumn> columnList = getColumnList(node);
	
	List< ? extends IConnection> conns = node.getIncomingConnections();
	if(conns!=null && conns.size()>0){
		IConnection conn = conns.get(0);
		incomingConnName = conn.getName();
	}
		
    String rejectConnName = null;
    List<? extends IConnection> rejectConns = node.getOutgoingConnections("REJECT");
    if(rejectConns != null && rejectConns.size() > 0) {
        IConnection rejectConn = rejectConns.get(0);
        rejectConnName = rejectConn.getName();
    }
    List<IMetadataColumn> rejectColumnList = null;
    IMetadataTable metadataTable = node.getMetadataFromConnector("REJECT");
    if(metadataTable != null) {
        rejectColumnList = metadataTable.getListColumns();      
    }
    
    List<? extends IConnection> outgoingConns = node.getOutgoingSortedConnections();
    for(IConnection conn : outgoingConns) {
        if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
            
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_4);
          
        }
    }
	
	List<Column> stmtStructure = getManager(dbmsId, cid).createColumnList(columnList, useFieldOptions, fieldOptions, addCols);
	
	Manager manager = null;
	boolean isDynamic = false;
	List<IMetadataTable> metadatas = node.getMetadataList();
	if ((metadatas!=null)&&(metadatas.size()>0)) {
		IMetadataTable metadata = metadatas.get(0);
		isDynamic = metadata.isDynamicSchema(); 
	}
	
    ////////////////////////////////////////////////////////////
    List<Column> colStruct = new ArrayList();
    for(Column colStmt:stmtStructure) {
    	if(!colStmt.isReplaced()&&!colStmt.isAddCol()) {
    		colStruct.add(colStmt);
    	}
    }
    
	if(isEnableDebug) {

    stringBuffer.append(TEXT_5);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_6);
    
	}
	
	if(isDynamic) {
	
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_8);
    
	if(columnList != null && columnList.size() > 0) {        
       manager = null;
    
      
//issue 0010346 Parallelization crash with "Drop table if exists and create"
Boolean isParallelize ="true".equalsIgnoreCase(ElementParameterParser.getValue(node, "__PARALLELIZE__"));
String dbms=ElementParameterParser.getValue(node, "__MAPPING__");
if (!isParallelize) {
//end issue 0010346 Parallelization crash with "Drop table if exists and create"
	manager = getManager(dbmsId, cid);
	String ending="";
	
	if("VECTORWISE".equalsIgnoreCase(dbmsId)) {
		ending="WITH STRUCTURE = VECTORWISE";
	}

	if(!("NONE").equals(tableAction)) {

        if(("DROP_CREATE").equals(tableAction)) {

    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(manager.getDropTableSQL());
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_16);
    
			if(isDynamic) {
				List< ? extends IConnection> conns_dynamic = node.getIncomingConnections();
				if(conns_dynamic!=null && conns_dynamic.size()>0){
					IConnection conn = conns_dynamic.get(0);
					String query=manager.getCreateTableSQL(stmtStructure);
					if(query.lastIndexOf("(")==query.length()-1) {

    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(query);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_20);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_21);
    stringBuffer.append(dbms==null?"":dbms.toLowerCase());
    stringBuffer.append(TEXT_22);
    stringBuffer.append(ending);
    stringBuffer.append(TEXT_23);
    
					} else {

    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(query);
    stringBuffer.append(TEXT_26);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_27);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_28);
    stringBuffer.append(dbms==null?"":dbms.toLowerCase());
    stringBuffer.append(TEXT_29);
    stringBuffer.append(ending);
    stringBuffer.append(TEXT_30);
    
					}
				}
			} else {

    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_32);
    stringBuffer.append(manager.getCreateTableSQL(stmtStructure));
    stringBuffer.append(TEXT_33);
    
			}

    stringBuffer.append(TEXT_34);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_35);
    				

        } else if(("CREATE").equals(tableAction)) {

    stringBuffer.append(TEXT_36);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_37);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_38);
    
			if(isDynamic) {
				List< ? extends IConnection> conns_dynamic = node.getIncomingConnections();
				if(conns_dynamic!=null && conns_dynamic.size()>0){
					IConnection conn = conns_dynamic.get(0);
					String query=manager.getCreateTableSQL(stmtStructure);
					if(query.lastIndexOf("(")==query.length()-1) {

    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_40);
    stringBuffer.append(query);
    stringBuffer.append(TEXT_41);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_42);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_43);
    stringBuffer.append(dbms==null?"":dbms.toLowerCase());
    stringBuffer.append(TEXT_44);
    stringBuffer.append(ending);
    stringBuffer.append(TEXT_45);
    
					} else {

    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_47);
    stringBuffer.append(query);
    stringBuffer.append(TEXT_48);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_49);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_50);
    stringBuffer.append(dbms==null?"":dbms.toLowerCase());
    stringBuffer.append(TEXT_51);
    stringBuffer.append(ending);
    stringBuffer.append(TEXT_52);
    
					}
				}
			} else {

    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_54);
    stringBuffer.append(manager.getCreateTableSQL(stmtStructure));
    stringBuffer.append(TEXT_55);
    
			}

    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_57);
    
        } else if(("CREATE_IF_NOT_EXISTS").equals(tableAction) || ("DROP_IF_EXISTS_AND_CREATE").equals(tableAction)) {

    
			//to fixed: bug8422
			if((cid.equals("talendLogs_DB") || cid.equals("talendStats_DB") || cid.equals("talendMeter_DB"))){

    stringBuffer.append(TEXT_58);
    //bug22719: informix use independent connection, should remove this code after use shared connection
		    if(INFORMIX.equalsIgnoreCase(dbmsId)){
			    String dbnameKey = ElementParameterParser.getValue(node, "__DBNAME__");
	    		String dbserverKey = ElementParameterParser.getValue(node, "__DBSERVER__");
			    String dbhostKey = ElementParameterParser.getValue(node, "__HOST__");
				String dbportKey = ElementParameterParser.getValue(node, "__PORT__");
				String tableSchemaKey = ElementParameterParser.getValue(node,"__SCHEMA_DB__");
		    
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(dbhostKey);
    stringBuffer.append(TEXT_61);
    stringBuffer.append(dbportKey);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(dbnameKey);
    stringBuffer.append(TEXT_63);
    stringBuffer.append(dbserverKey);
    stringBuffer.append(TEXT_64);
    stringBuffer.append(tableSchemaKey);
    stringBuffer.append(TEXT_65);
    stringBuffer.append(table);
    stringBuffer.append(TEXT_66);
    }else{
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_69);
    stringBuffer.append(table);
    stringBuffer.append(TEXT_70);
    }
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_74);
    
			}

    stringBuffer.append(TEXT_75);
     
			/*
				case multi connection access the same db, that maybe have a locked in System table(e.g mssql).
				so in mssql use "Select" checked the table whether exist.
			*/
			//TODO we will use like "select" sql intead of use system table to checked table?
			if(MSSQL.equalsIgnoreCase(dbmsId)) {

    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_77);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_80);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_84);
    
			} else if (ORACLE.equalsIgnoreCase(dbmsId)) {

    stringBuffer.append(TEXT_85);
    stringBuffer.append(manager.generateCode4TabelExist());
    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_87);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_88);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_90);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_91);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_95);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_96);
    
			} else if ( SYBASE.equalsIgnoreCase(dbmsId) || POSTGRESQL.equalsIgnoreCase(dbmsId)
						|| INFORMIX.equalsIgnoreCase(dbmsId) || DB2.equalsIgnoreCase(dbmsId) ) {

    stringBuffer.append(TEXT_97);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_98);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_99);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_102);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_107);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_108);
    stringBuffer.append(table);
    stringBuffer.append(TEXT_109);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_114);
    			
			} else {

    stringBuffer.append(TEXT_115);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_118);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_119);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_120);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_121);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_122);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_123);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_124);
    stringBuffer.append(table);
    stringBuffer.append(TEXT_125);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_126);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_127);
    
			}

    
            if(("CREATE_IF_NOT_EXISTS").equals(tableAction)) {

    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_129);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_130);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_131);
    
			if(isDynamic) {
				List< ? extends IConnection> conns_dynamic = node.getIncomingConnections();
				if(conns_dynamic!=null && conns_dynamic.size()>0){
					IConnection conn = conns_dynamic.get(0);
					String query=manager.getCreateTableSQL(stmtStructure);
					if(query.lastIndexOf("(")==query.length()-1) {

    stringBuffer.append(TEXT_132);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_133);
    stringBuffer.append(query);
    stringBuffer.append(TEXT_134);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_135);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_136);
    stringBuffer.append(dbms==null?"":dbms.toLowerCase());
    stringBuffer.append(TEXT_137);
    stringBuffer.append(ending);
    stringBuffer.append(TEXT_138);
    
					} else {

    stringBuffer.append(TEXT_139);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_140);
    stringBuffer.append(query);
    stringBuffer.append(TEXT_141);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_142);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_143);
    stringBuffer.append(dbms==null?"":dbms.toLowerCase());
    stringBuffer.append(TEXT_144);
    stringBuffer.append(ending);
    stringBuffer.append(TEXT_145);
    
					}
				}
			} else {

    stringBuffer.append(TEXT_146);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_147);
    stringBuffer.append(manager.getCreateTableSQL(stmtStructure));
    stringBuffer.append(TEXT_148);
    
			}

    stringBuffer.append(TEXT_149);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_150);
    
            } else {

    stringBuffer.append(TEXT_151);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_152);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_153);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_154);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_155);
    stringBuffer.append(manager.getDropTableSQL());
    stringBuffer.append(TEXT_156);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_157);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_158);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_159);
    
			if(isDynamic) {
				List< ? extends IConnection> conns_dynamic = node.getIncomingConnections();
				if(conns_dynamic!=null && conns_dynamic.size()>0){
					IConnection conn = conns_dynamic.get(0);
					String query=manager.getCreateTableSQL(stmtStructure);
					if(query.lastIndexOf("(")==query.length()-1) {

    stringBuffer.append(TEXT_160);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_161);
    stringBuffer.append(query);
    stringBuffer.append(TEXT_162);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_163);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_164);
    stringBuffer.append(dbms==null?"":dbms.toLowerCase());
    stringBuffer.append(TEXT_165);
    stringBuffer.append(ending);
    stringBuffer.append(TEXT_166);
    
					} else {

    stringBuffer.append(TEXT_167);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_168);
    stringBuffer.append(query);
    stringBuffer.append(TEXT_169);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_170);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_171);
    stringBuffer.append(dbms==null?"":dbms.toLowerCase());
    stringBuffer.append(TEXT_172);
    stringBuffer.append(ending);
    stringBuffer.append(TEXT_173);
    
					}
				}
			} else {

    stringBuffer.append(TEXT_174);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_175);
    stringBuffer.append(manager.getCreateTableSQL(stmtStructure));
    stringBuffer.append(TEXT_176);
    
			}

    stringBuffer.append(TEXT_177);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_178);
    
			}

    
			//to fixed: bug8422
			if((cid.equals("talendLogs_DB") || cid.equals("talendStats_DB") || cid.equals("talendMeter_DB"))){

    stringBuffer.append(TEXT_179);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_180);
    
			}

    stringBuffer.append(TEXT_181);
    
        } else if(("CLEAR").equals(tableAction)) {

    stringBuffer.append(TEXT_182);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_183);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_184);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_185);
    stringBuffer.append(manager.getDeleteTableSQL());
    stringBuffer.append(TEXT_186);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_187);
    
        }else if(("TRUNCATE").equals(tableAction)) {

    stringBuffer.append(TEXT_188);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_189);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_190);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_191);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_192);
    stringBuffer.append(manager.getSelectionSQL());
    stringBuffer.append(TEXT_193);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_194);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_195);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_196);
    stringBuffer.append(manager.getTruncateTableSQL());
    stringBuffer.append(TEXT_197);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_198);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_199);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_200);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_201);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_202);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_203);
    
		} else if(("TRUNCATE_REUSE_STORAGE").equals(tableAction)) {

    stringBuffer.append(TEXT_204);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_205);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_206);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_207);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_208);
    stringBuffer.append(manager.getSelectionSQL());
    stringBuffer.append(TEXT_209);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_210);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_211);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_212);
    stringBuffer.append(manager.getTruncateReuseStroageTableSQL());
    stringBuffer.append(TEXT_213);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_214);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_215);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_216);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_217);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_218);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_219);
    
		}         
	}
//issue 0010346 Parallelization crash with "Drop table if exists and create"
}
//end issue 0010346 Parallelization crash with "Drop table if exists and create"

       
    Map<String, StringBuilder> actionSQLMap = getManager(dbmsId, cid).createProcessSQL(stmtStructure);
    StringBuilder insertColName = actionSQLMap.get(INSERT_COLUMN_NAME);   
    StringBuilder insertValueStmt = actionSQLMap.get(INSERT_VALUE_STMT);    
    StringBuilder updateSetStmt = actionSQLMap.get(UPDATE_SET_STMT);    
    StringBuilder updateWhereStmt = actionSQLMap.get(UPDATE_WHERE_STMT);
    StringBuilder deleteWhereStmt = actionSQLMap.get(DELETE_WHERE_STMT);
    
	if(("INSERT").equals(dataAction)) {
		List< ? extends IConnection> conns_dynamic = node.getIncomingConnections();
		if(conns_dynamic!=null && conns_dynamic.size()>0){
			IConnection conn = conns_dynamic.get(0);
			if(!("".equals(insertColName.toString()))) {

    stringBuffer.append(TEXT_220);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_221);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_222);
    stringBuffer.append(insertColName.toString());
    stringBuffer.append(TEXT_223);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_224);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_225);
    stringBuffer.append(dbmsId );
    stringBuffer.append(TEXT_226);
    stringBuffer.append(insertValueStmt.toString());
    stringBuffer.append(TEXT_227);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_228);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_229);
    			} else {

    stringBuffer.append(TEXT_230);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_231);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_232);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_233);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_234);
    stringBuffer.append(dbmsId );
    stringBuffer.append(TEXT_235);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_236);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_237);
    
			}
		}
	    
    stringBuffer.append(TEXT_238);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_239);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_240);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_241);
    
	} else if (("UPDATE").equals(dataAction)) {
		List< ? extends IConnection> conns_dynamic = node.getIncomingConnections();
		if(conns_dynamic!=null && conns_dynamic.size()>0){
			IConnection conn = conns_dynamic.get(0);
			if(!("".equals(updateSetStmt.toString()))) {

    stringBuffer.append(TEXT_242);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_243);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_244);
    stringBuffer.append(updateSetStmt.toString());
    stringBuffer.append(TEXT_245);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_246);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_247);
    stringBuffer.append(dbmsId );
    stringBuffer.append(TEXT_248);
    stringBuffer.append(updateWhereStmt.toString());
    stringBuffer.append(TEXT_249);
    			} else {

    stringBuffer.append(TEXT_250);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_251);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_252);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_253);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_254);
    stringBuffer.append(dbmsId );
    stringBuffer.append(TEXT_255);
    stringBuffer.append(updateWhereStmt.toString());
    stringBuffer.append(TEXT_256);
    
			}
		}
	    
    stringBuffer.append(TEXT_257);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_258);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_259);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_260);
    		
	} else if (("INSERT_OR_UPDATE").equals(dataAction)) {
		List< ? extends IConnection> conns_dynamic = node.getIncomingConnections();
		if(conns_dynamic!=null && conns_dynamic.size()>0){
			IConnection conn = conns_dynamic.get(0);
			if(!("".equals(insertColName.toString()))) {

    stringBuffer.append(TEXT_261);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_262);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_263);
    stringBuffer.append(insertColName.toString());
    stringBuffer.append(TEXT_264);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_265);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_266);
    stringBuffer.append(dbmsId );
    stringBuffer.append(TEXT_267);
    stringBuffer.append(insertValueStmt.toString());
    stringBuffer.append(TEXT_268);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_269);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_270);
    			} else {

    stringBuffer.append(TEXT_271);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_272);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_273);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_274);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_275);
    stringBuffer.append(dbmsId );
    stringBuffer.append(TEXT_276);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_277);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_278);
    
			}
			if(!("".equals(updateSetStmt.toString()))) {

    stringBuffer.append(TEXT_279);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_280);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_281);
    stringBuffer.append(updateSetStmt.toString());
    stringBuffer.append(TEXT_282);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_283);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_284);
    stringBuffer.append(dbmsId );
    stringBuffer.append(TEXT_285);
    stringBuffer.append(updateWhereStmt.toString());
    stringBuffer.append(TEXT_286);
    			} else {

    stringBuffer.append(TEXT_287);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_288);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_289);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_290);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_291);
    stringBuffer.append(dbmsId );
    stringBuffer.append(TEXT_292);
    stringBuffer.append(updateWhereStmt.toString());
    stringBuffer.append(TEXT_293);
    
			}
		}
	    
    stringBuffer.append(TEXT_294);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_295);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_296);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_297);
    stringBuffer.append(updateWhereStmt.toString());
    stringBuffer.append(TEXT_298);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_299);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_300);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_301);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_302);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_303);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_304);
    
	} else if (("UPDATE_OR_INSERT").equals(dataAction)) {
		List< ? extends IConnection> conns_dynamic = node.getIncomingConnections();
		if(conns_dynamic!=null && conns_dynamic.size()>0){
			IConnection conn = conns_dynamic.get(0);
			if(!("".equals(insertColName.toString()))) {

    stringBuffer.append(TEXT_305);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_306);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_307);
    stringBuffer.append(insertColName.toString());
    stringBuffer.append(TEXT_308);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_309);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_310);
    stringBuffer.append(dbmsId );
    stringBuffer.append(TEXT_311);
    stringBuffer.append(insertValueStmt.toString());
    stringBuffer.append(TEXT_312);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_313);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_314);
    			} else {

    stringBuffer.append(TEXT_315);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_316);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_317);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_318);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_319);
    stringBuffer.append(dbmsId );
    stringBuffer.append(TEXT_320);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_321);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_322);
    
			}
			if(!("".equals(updateSetStmt.toString()))) {

    stringBuffer.append(TEXT_323);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_324);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_325);
    stringBuffer.append(updateSetStmt.toString());
    stringBuffer.append(TEXT_326);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_327);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_328);
    stringBuffer.append(dbmsId );
    stringBuffer.append(TEXT_329);
    stringBuffer.append(updateWhereStmt.toString());
    stringBuffer.append(TEXT_330);
    			} else {

    stringBuffer.append(TEXT_331);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_332);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_333);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_334);
    stringBuffer.append(columnList.get(columnList.size()-1));
    stringBuffer.append(TEXT_335);
    stringBuffer.append(dbmsId );
    stringBuffer.append(TEXT_336);
    stringBuffer.append(updateWhereStmt.toString());
    stringBuffer.append(TEXT_337);
    
			}
		}
	    
    stringBuffer.append(TEXT_338);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_339);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_340);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_341);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_342);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_343);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_344);
    			
	} else if (("DELETE").equals(dataAction)) {
	    
    stringBuffer.append(TEXT_345);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_346);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_347);
    stringBuffer.append(deleteWhereStmt.toString());
    stringBuffer.append(TEXT_348);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_349);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_350);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_351);
    		
	}
    if(isEnableDebug) {
        
    stringBuffer.append(TEXT_352);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_353);
    
	     if(("INSERT").equals(dataAction) || ("INSERT_IGNORE").equals(dataAction)) {

    stringBuffer.append(TEXT_354);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_355);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_356);
    
		} else if (("UPDATE").equals(dataAction)) {

    stringBuffer.append(TEXT_357);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_358);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_359);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_360);
    		
		} else if (("INSERT_OR_UPDATE").equals(dataAction) || ("UPDATE_OR_INSERT").equals(dataAction)) {

    stringBuffer.append(TEXT_361);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_362);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_363);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_364);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_365);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_366);
    
		} else if (("DELETE").equals(dataAction)) {

    stringBuffer.append(TEXT_367);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_368);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_369);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_370);
    		
		} else if (("REPLACE").equals(dataAction)) {//mysql

    stringBuffer.append(TEXT_371);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_372);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_373);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_374);
    
		} else if (("INSERT_ON_DUPLICATE_KEY_UPDATE").equals(dataAction)) {//mysql

    stringBuffer.append(TEXT_375);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_376);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_377);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_378);
    
		}else if (("INSERT_IF_NOT_EXIST").equals(dataAction)) {//MSSQL

    stringBuffer.append(TEXT_379);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_380);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_381);
    
		}

    
    }
	}
	
    stringBuffer.append(TEXT_382);
    
	} //end isDynamic
	
	boolean isParallelize ="true".equalsIgnoreCase(ElementParameterParser.getValue(node, "__PARALLELIZE__"));
	
	if (isParallelize) { // bug0014422
		String tAsyncIn_cid = "";
		if(conns!=null && conns.size() > 0) {
			tAsyncIn_cid = conns.get(0).getSource().getUniqueName();
		}
		if(!useExistingConnection) {
	    	if(!("").equals(commitEvery)&&!("0").equals(commitEvery)) {

    stringBuffer.append(TEXT_383);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_384);
    stringBuffer.append(tAsyncIn_cid);
    stringBuffer.append(TEXT_385);
    
	    	}
    	}
    	if (useBatchSize) {
		    if(!("").equals(batchSize)&&!("0").equals(batchSize)) {

    stringBuffer.append(TEXT_386);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_387);
    stringBuffer.append(tAsyncIn_cid);
    stringBuffer.append(TEXT_388);
    
		    }
    	}
	} // end bug0014422
	
    if(incomingConnName != null && columnList != null){
        
    stringBuffer.append(TEXT_389);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_390);
    	
        if(("INSERT").equals(dataAction)) {
            int counter = 1;
            for(Column column : colStruct) {
                if(column.isInsertable()) {
                    String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getColumn().getTalendType(), column.getColumn().isNullable());
                    
    stringBuffer.append(TEXT_391);
    stringBuffer.append(getManager(dbmsId, cid).generateSetStmt(typeToGenerate, column, counter, incomingConnName, cid, NORMAL_TYPE));
    
                    if(isEnableDebug) {
                        
    stringBuffer.append(TEXT_392);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_393);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, column, incomingConnName, cid, "query_" , counter, "insertSQLSplits_"));
    stringBuffer.append(TEXT_394);
    
                    }                    
                    counter++;                        
                }
            }
			if(isDynamic) {
				Column dynamicColumn = getColumn(columnList.get(columnList.size()-1));
				String typeToGenerate = JavaTypesManager.getTypeToGenerate(dynamicColumn.getColumn().getTalendType(), dynamicColumn.getColumn().isNullable());
				if("Dynamic".equals(typeToGenerate)) {
				
    stringBuffer.append(TEXT_395);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_396);
    stringBuffer.append(dynamicColumn.getName());
    stringBuffer.append(TEXT_397);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_398);
    stringBuffer.append(counter-1);
    stringBuffer.append(TEXT_399);
    stringBuffer.append(dbmsId);
    stringBuffer.append(TEXT_400);
    
				}
			}
            if(isEnableDebug) {
                
    stringBuffer.append(TEXT_401);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_402);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_403);
    
            }            
    		
    stringBuffer.append(TEXT_404);
    if (rejectConnName == null && useBatchSize) { // whether use batch
    		
    stringBuffer.append(TEXT_405);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_406);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_407);
    if (useBatchSize && !("").equals(batchSize) && !("0").equals(batchSize)) {
    		  
    stringBuffer.append(TEXT_408);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_409);
    
    		  }
    stringBuffer.append(TEXT_410);
    
    		}else {    		
    		
    stringBuffer.append(TEXT_411);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_412);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_413);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_414);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_415);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_416);
    
                if (("true").equals(dieOnError)) {
                
    stringBuffer.append(TEXT_417);
    
                } else {
                    if(rejectConnName != null && rejectColumnList != null && rejectColumnList.size() > 0) {
                
    stringBuffer.append(TEXT_418);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_419);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_420);
    
                        for(IMetadataColumn column : columnList) {
                        
    stringBuffer.append(TEXT_421);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_422);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_423);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_424);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_425);
    
                        }
                        
    stringBuffer.append(TEXT_426);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_427);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_428);
    stringBuffer.append(TEXT_429);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_430);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_431);
    
                    } else {
                    
    stringBuffer.append(TEXT_432);
    
                    }
                } 
                
    stringBuffer.append(TEXT_433);
    
            } // end whether use batch
            
    
    	} else if(("UPDATE").equals(dataAction)) {
    	    int counterCol = 1;
            for(Column column : colStruct) {
                if(column.isUpdatable()) {
                    String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getColumn().getTalendType(), column.getColumn().isNullable());
                    
    stringBuffer.append(TEXT_434);
    stringBuffer.append(getManager(dbmsId, cid).generateSetStmt(typeToGenerate, column, counterCol, incomingConnName, cid, NORMAL_TYPE));
    
                    if(isEnableDebug) {
                        
    stringBuffer.append(TEXT_435);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_436);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, column, incomingConnName, cid, "query_" , counterCol, "updateSQLSplits_"));
    stringBuffer.append(TEXT_437);
    
                    }                    
                    counterCol++;                   
                }
            }
			if(isDynamic) {
				Column dynamicColumn = getColumn(columnList.get(columnList.size()-1));
				String typeToGenerate = JavaTypesManager.getTypeToGenerate(dynamicColumn.getColumn().getTalendType(), dynamicColumn.getColumn().isNullable());
				if("Dynamic".equals(typeToGenerate)) {
				
    stringBuffer.append(TEXT_438);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_439);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_440);
    stringBuffer.append(dynamicColumn.getName());
    stringBuffer.append(TEXT_441);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_442);
    stringBuffer.append(counterCol-1);
    stringBuffer.append(TEXT_443);
    stringBuffer.append(dbmsId);
    stringBuffer.append(TEXT_444);
    
				}
			}
    
            for(Column column : colStruct) {
                if(column.isUpdateKey()) {
                    String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getColumn().getTalendType(), column.getColumn().isNullable());
					String dbType = column.getColumn().getType();
                    
    stringBuffer.append(TEXT_445);
     
                    //#############for feature:2880 and 6980
                    if(whereSupportNull && column.getColumn().isNullable()) { 
    stringBuffer.append(TEXT_446);
    stringBuffer.append(getManager(dbmsId, cid, node).generateSetBooleanForNullableKeyStmt(column, counterCol, incomingConnName, cid, NORMAL_TYPE));
      
					if(isEnableDebug) {
                        
    stringBuffer.append(TEXT_447);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_448);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, column, incomingConnName, cid, "query_" , counterCol, "updateSQLSplits_", "(("+incomingConnName+"."+column.getName()+"==null)?1:0)"));
    stringBuffer.append(TEXT_449);
    
                    }
                    	counterCol++;
                       }
                     //#############                      
                    
    stringBuffer.append(TEXT_450);
    stringBuffer.append(TEXT_451);
    stringBuffer.append(getManager(dbmsId, cid).generateSetStmt(typeToGenerate, column, counterCol, incomingConnName, cid, NORMAL_TYPE, " + count_"));
    
                    if(isEnableDebug) {
                        
    stringBuffer.append(TEXT_452);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_453);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, column, incomingConnName, cid, "query_" , counterCol, "updateSQLSplits_"));
    stringBuffer.append(TEXT_454);
    
                    }                    
                    counterCol++;
                }
            }
            if(isEnableDebug) {
                
    stringBuffer.append(TEXT_455);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_456);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_457);
    
            }            
    		
    stringBuffer.append(TEXT_458);
    if (rejectConnName == null && useBatchSize) { // whether use batch
    		
    stringBuffer.append(TEXT_459);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_460);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_461);
    if (useBatchSize && !("").equals(batchSize) && !("0").equals(batchSize)) {
    		  
    stringBuffer.append(TEXT_462);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_463);
    
    		  }
    stringBuffer.append(TEXT_464);
    
    		}else {    		
    		
    stringBuffer.append(TEXT_465);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_466);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_467);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_468);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_469);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_470);
    
                if (("true").equals(dieOnError)) {
                    
    stringBuffer.append(TEXT_471);
    
                } else {
                    if(rejectConnName != null && rejectColumnList != null && rejectColumnList.size() > 0) {
                        
    stringBuffer.append(TEXT_472);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_473);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_474);
    
                        for(IMetadataColumn column : columnList) {
                            
    stringBuffer.append(TEXT_475);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_476);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_477);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_478);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_479);
    
                        }
                        
    stringBuffer.append(TEXT_480);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_481);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_482);
    stringBuffer.append(TEXT_483);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_484);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_485);
    
                    } else {
                        
    stringBuffer.append(TEXT_486);
    
                    }
                } 
                
    stringBuffer.append(TEXT_487);
    
            } // end whether use batch
            
    
    	} else if (("INSERT_OR_UPDATE").equals(dataAction)) {
    		int columnIndex = 1;
            for(Column column : colStruct) {
                if(column.isUpdateKey()) {
                    String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getColumn().getTalendType(), column.getColumn().isNullable());
                    String dbType = column.getColumn().getType();
                    
    stringBuffer.append(TEXT_488);
     
                    //#############for feature:2880 and 6980
                    if(whereSupportNull && column.getColumn().isNullable()) { 
    stringBuffer.append(TEXT_489);
    stringBuffer.append(getManager(dbmsId, cid, node).generateSetBooleanForNullableKeyStmt(column, columnIndex, incomingConnName, cid, NORMAL_TYPE));
      
                    	columnIndex++;
                       }
                     //#############                      
                    
    stringBuffer.append(TEXT_490);
    stringBuffer.append(getManager(dbmsId, cid).generateSetStmt(typeToGenerate, column, columnIndex, incomingConnName, cid, NORMAL_TYPE));
    
                    columnIndex++;
                }
            }
    		
    stringBuffer.append(TEXT_491);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_492);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_493);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_494);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_495);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_496);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_497);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_498);
    
    			int counterCol = 1;
                for(Column column : colStruct) {
                    if(column.isUpdatable()) {
                        String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getColumn().getTalendType(), column.getColumn().isNullable());
                        
    stringBuffer.append(TEXT_499);
    stringBuffer.append(getManager(dbmsId, cid).generateSetStmt(typeToGenerate, column, counterCol, incomingConnName, cid, UPDATE_TYPE));
    
                        if(isEnableDebug) {
                            
    stringBuffer.append(TEXT_500);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_501);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, column, incomingConnName, cid, "query_" , counterCol, "updateSQLSplits_"));
    stringBuffer.append(TEXT_502);
    
                        }                        
                        counterCol++;
                    }
                }
				if(isDynamic) {
					Column dynamicColumn = getColumn(columnList.get(columnList.size()-1));
					String typeToGenerate = JavaTypesManager.getTypeToGenerate(dynamicColumn.getColumn().getTalendType(), dynamicColumn.getColumn().isNullable());
					if("Dynamic".equals(typeToGenerate)) {
					
    stringBuffer.append(TEXT_503);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_504);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_505);
    stringBuffer.append(dynamicColumn.getName());
    stringBuffer.append(TEXT_506);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_507);
    stringBuffer.append(counterCol-1);
    stringBuffer.append(TEXT_508);
    stringBuffer.append(dbmsId);
    stringBuffer.append(TEXT_509);
    
					}
				}
                for(Column column : colStruct) {
                    if(column.isUpdateKey()) {
                        String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getColumn().getTalendType(), column.getColumn().isNullable());
                        String dbType = column.getColumn().getType();
                        
     
	                    //#############for feature:2880
	                    if(whereSupportNull && column.getColumn().isNullable()) { 
    stringBuffer.append(TEXT_510);
    stringBuffer.append(getManager(dbmsId, cid, node).generateSetBooleanForNullableKeyStmt(column, counterCol, incomingConnName, cid, UPDATE_TYPE));
    stringBuffer.append(TEXT_511);
      
		                     if(isEnableDebug) {
	                        
    stringBuffer.append(TEXT_512);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_513);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, column, incomingConnName, cid, "query_" , counterCol, "updateSQLSplits_", "(("+incomingConnName+"."+column.getName()+"==null)?1:0)"));
    stringBuffer.append(TEXT_514);
    
	                    	} 
	                    	counterCol++;
	                       }
	                     //#############                      
	                    
    stringBuffer.append(TEXT_515);
    stringBuffer.append(getManager(dbmsId, cid).generateSetStmt(typeToGenerate, column, counterCol, incomingConnName, cid, UPDATE_TYPE, " + count_"));
    
                        if(isEnableDebug) {
                            
    stringBuffer.append(TEXT_516);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_517);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, column, incomingConnName, cid, "query_" , counterCol, "updateSQLSplits_"));
    stringBuffer.append(TEXT_518);
    
                        }                        
                        counterCol++;                   
                    }
                }
                if(isEnableDebug) {
                    
    stringBuffer.append(TEXT_519);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_520);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_521);
    
                }                
    			
    stringBuffer.append(TEXT_522);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_523);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_524);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_525);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_526);
    
                    if (("true").equals(dieOnError)) {
                        
    stringBuffer.append(TEXT_527);
    
                    } else {
                        if(rejectConnName != null && rejectColumnList != null && rejectColumnList.size() > 0) {
                            
    stringBuffer.append(TEXT_528);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_529);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_530);
    
                            for(IMetadataColumn column : columnList) {
                                
    stringBuffer.append(TEXT_531);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_532);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_533);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_534);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_535);
    
                            }
                            
    stringBuffer.append(TEXT_536);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_537);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_538);
    stringBuffer.append(TEXT_539);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_540);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_541);
    
                        } else {
                            
    stringBuffer.append(TEXT_542);
    
                        }
                    } 
                    
    stringBuffer.append(TEXT_543);
    
    			int counterInsert = 1;
                for(Column columnInsert : colStruct) {
                    if(columnInsert.isInsertable()) {
                        String typeToGenerate = JavaTypesManager.getTypeToGenerate(columnInsert.getColumn().getTalendType(), columnInsert.getColumn().isNullable());
                        
    stringBuffer.append(TEXT_544);
    stringBuffer.append(getManager(dbmsId, cid).generateSetStmt(typeToGenerate, columnInsert, counterInsert, incomingConnName, cid, INSERT_TYPE));
    
                        if(isEnableDebug) {
                            
    stringBuffer.append(TEXT_545);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_546);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, columnInsert, incomingConnName, cid, "query_" ,  counterInsert, "insertSQLSplits_"));
    stringBuffer.append(TEXT_547);
    
                        }                        
                        counterInsert++;
                    }
                }
				if(isDynamic) {
					Column dynamicColumn = getColumn(columnList.get(columnList.size()-1));
					String typeToGenerate = JavaTypesManager.getTypeToGenerate(dynamicColumn.getColumn().getTalendType(), dynamicColumn.getColumn().isNullable());
					if("Dynamic".equals(typeToGenerate)) {
					
    stringBuffer.append(TEXT_548);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_549);
    stringBuffer.append(dynamicColumn.getName());
    stringBuffer.append(TEXT_550);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_551);
    stringBuffer.append(counterInsert-1);
    stringBuffer.append(TEXT_552);
    stringBuffer.append(dbmsId);
    stringBuffer.append(TEXT_553);
    
					}
				}
                if(isEnableDebug) {
                    
    stringBuffer.append(TEXT_554);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_555);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_556);
    
                }                
    			
    stringBuffer.append(TEXT_557);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_558);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_559);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_560);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_561);
    
                    if (("true").equals(dieOnError)) {
                        
    stringBuffer.append(TEXT_562);
    
                    } else {
                        if(rejectConnName != null && rejectColumnList != null && rejectColumnList.size() > 0) {
                            
    stringBuffer.append(TEXT_563);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_564);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_565);
    
                            for(IMetadataColumn column : columnList) {
                                
    stringBuffer.append(TEXT_566);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_567);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_568);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_569);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_570);
    
                            }
                            
    stringBuffer.append(TEXT_571);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_572);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_573);
    stringBuffer.append(TEXT_574);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_575);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_576);
    
                        } else {
                            
    stringBuffer.append(TEXT_577);
    
                        }
                    } 
                    
    stringBuffer.append(TEXT_578);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_579);
    
    	} else if (("UPDATE_OR_INSERT").equals(dataAction)) {
    	    
    stringBuffer.append(TEXT_580);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_581);
    
    		int counterColUpdate = 1;
            for(Column columnUpdate : colStruct) {
                if(columnUpdate.isUpdatable()) {
                    String typeToGenerate = JavaTypesManager.getTypeToGenerate(columnUpdate.getColumn().getTalendType(), columnUpdate.getColumn().isNullable());
                    
    stringBuffer.append(TEXT_582);
    stringBuffer.append(getManager(dbmsId, cid).generateSetStmt(typeToGenerate, columnUpdate, counterColUpdate, incomingConnName, cid, UPDATE_TYPE));
    
                    if(isEnableDebug) {
                        
    stringBuffer.append(TEXT_583);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_584);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, columnUpdate, incomingConnName, cid, "query_" , counterColUpdate, "updateSQLSplits_"));
    stringBuffer.append(TEXT_585);
    
                    }                    
                    counterColUpdate++;
                }
            }
			if(isDynamic) {
				Column dynamicColumn = getColumn(columnList.get(columnList.size()-1));
				String typeToGenerate = JavaTypesManager.getTypeToGenerate(dynamicColumn.getColumn().getTalendType(), dynamicColumn.getColumn().isNullable());
				if("Dynamic".equals(typeToGenerate)) {
				
    stringBuffer.append(TEXT_586);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_587);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_588);
    stringBuffer.append(dynamicColumn.getName());
    stringBuffer.append(TEXT_589);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_590);
    stringBuffer.append(counterColUpdate-1);
    stringBuffer.append(TEXT_591);
    stringBuffer.append(dbmsId);
    stringBuffer.append(TEXT_592);
    
				}
			}
    
            for(Column columnUpdate : colStruct) {
                if(columnUpdate.isUpdateKey()) {
                    String typeToGenerate = JavaTypesManager.getTypeToGenerate(columnUpdate.getColumn().getTalendType(), columnUpdate.getColumn().isNullable());
                    String dbType = columnUpdate.getColumn().getType();
                    
    stringBuffer.append(TEXT_593);
     
                    //#############for feature:2880 and 6980
                    if(whereSupportNull && columnUpdate.getColumn().isNullable()) { 
    stringBuffer.append(TEXT_594);
    stringBuffer.append(getManager(dbmsId, cid, node).generateSetBooleanForNullableKeyStmt(columnUpdate, counterColUpdate, incomingConnName, cid, UPDATE_TYPE));
      
	                    if(isEnableDebug) {
	                        
    stringBuffer.append(TEXT_595);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_596);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, columnUpdate, incomingConnName, cid, "query_" , counterColUpdate, "updateSQLSplits_", "(("+incomingConnName+"."+columnUpdate.getName()+"==null)?1:0)"));
    stringBuffer.append(TEXT_597);
    
	                    	}
                    	counterColUpdate++;
                       }
                     //#############                      
                    
    stringBuffer.append(TEXT_598);
    stringBuffer.append(TEXT_599);
    stringBuffer.append(getManager(dbmsId, cid).generateSetStmt(typeToGenerate, columnUpdate, counterColUpdate, incomingConnName, cid, UPDATE_TYPE, " + count_"));
    
                    if(isEnableDebug) {
                        
    stringBuffer.append(TEXT_600);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_601);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, columnUpdate, incomingConnName, cid, "query_" , counterColUpdate, "updateSQLSplits_"));
    stringBuffer.append(TEXT_602);
    
                    }                    
                    counterColUpdate++;
                }
            }
            if(isEnableDebug) {
                
    stringBuffer.append(TEXT_603);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_604);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_605);
    
            }            
    		
    stringBuffer.append(TEXT_606);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_607);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_608);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_609);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_610);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_611);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_612);
    
                if (("true").equals(dieOnError)) {
                    
    stringBuffer.append(TEXT_613);
    
                } else {
                    if(rejectConnName != null && rejectColumnList != null && rejectColumnList.size() > 0) {
                        
    stringBuffer.append(TEXT_614);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_615);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_616);
    
                        for(IMetadataColumn column : columnList) {
                            
    stringBuffer.append(TEXT_617);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_618);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_619);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_620);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_621);
    
                        }
                        
    stringBuffer.append(TEXT_622);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_623);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_624);
    stringBuffer.append(TEXT_625);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_626);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_627);
    
                    } else {
                        
    stringBuffer.append(TEXT_628);
    
                    }
                } 
                
    stringBuffer.append(TEXT_629);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_630);
    
            	if(isEnableDebug) {
            	
    stringBuffer.append(TEXT_631);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_632);
    
        		}
        		int counter = 1;
                for(Column column : colStruct) {
                    if(column.isInsertable()) {
                        String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getColumn().getTalendType(), column.getColumn().isNullable());
                        
    stringBuffer.append(TEXT_633);
    stringBuffer.append(getManager(dbmsId, cid).generateSetStmt(typeToGenerate, column, counter, incomingConnName, cid, INSERT_TYPE));
    
                        if(isEnableDebug) {
                            
    stringBuffer.append(TEXT_634);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_635);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, column, incomingConnName, cid, "query_" , counter, "insertSQLSplits_"));
    stringBuffer.append(TEXT_636);
    
                        }                        
                        counter++;
                    }
                }
				if(isDynamic) {
					Column dynamicColumn = getColumn(columnList.get(columnList.size()-1));
					String typeToGenerate = JavaTypesManager.getTypeToGenerate(dynamicColumn.getColumn().getTalendType(), dynamicColumn.getColumn().isNullable());
					if("Dynamic".equals(typeToGenerate)) {
					
    stringBuffer.append(TEXT_637);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_638);
    stringBuffer.append(dynamicColumn.getName());
    stringBuffer.append(TEXT_639);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_640);
    stringBuffer.append(counter-1);
    stringBuffer.append(TEXT_641);
    stringBuffer.append(dbmsId);
    stringBuffer.append(TEXT_642);
    
					}
				}
                if(isEnableDebug) {
                    
    stringBuffer.append(TEXT_643);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_644);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_645);
    
                }                
        		
    stringBuffer.append(TEXT_646);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_647);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_648);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_649);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_650);
    
                    if (("true").equals(dieOnError)) {
                        
    stringBuffer.append(TEXT_651);
    
                    } else {
                        if(rejectConnName != null && rejectColumnList != null && rejectColumnList.size() > 0) {
                            
    stringBuffer.append(TEXT_652);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_653);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_654);
    
                            for(IMetadataColumn column : columnList) {
                                
    stringBuffer.append(TEXT_655);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_656);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_657);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_658);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_659);
    
                            }
                            
    stringBuffer.append(TEXT_660);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_661);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_662);
    stringBuffer.append(TEXT_663);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_664);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_665);
    
                        } else {
                            
    stringBuffer.append(TEXT_666);
    
                        }
                    } 
                    
    stringBuffer.append(TEXT_667);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_668);
    
    	
    	} else if (("DELETE").equals(dataAction)) {
    	    int keyCounter = 1;
            for(Column column : colStruct) {
                if(column.isDeleteKey()) {
                    String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getColumn().getTalendType(), column.getColumn().isNullable());
                    String dbType = column.getColumn().getType();
                    
    stringBuffer.append(TEXT_669);
     
                    //#############for feature:2880 and 6980
                    if(whereSupportNull && column.getColumn().isNullable()) { 
    stringBuffer.append(TEXT_670);
    stringBuffer.append(getManager(dbmsId, cid, node).generateSetBooleanForNullableKeyStmt(column, keyCounter, incomingConnName, cid, NORMAL_TYPE));
      
	                    if(isEnableDebug) {
	                        
    stringBuffer.append(TEXT_671);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_672);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, column, incomingConnName, cid, "query_" , keyCounter, "deleteSQLSplits_", "(("+incomingConnName+"."+column.getName()+"==null)?1:0)"));
    stringBuffer.append(TEXT_673);
    
	                    	}
                    	 keyCounter++;
                       }
                     //#############                      
                    
    stringBuffer.append(TEXT_674);
    stringBuffer.append(TEXT_675);
    stringBuffer.append(getManager(dbmsId, cid).generateSetStmt(typeToGenerate, column, keyCounter, incomingConnName, cid, NORMAL_TYPE));
    
                    if(isEnableDebug) {
                        
    stringBuffer.append(TEXT_676);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_677);
    stringBuffer.append(getManager(dbmsId, cid).retrieveSQL(typeToGenerate, column, incomingConnName, cid, "query_" , keyCounter, "deleteSQLSplits_"));
    stringBuffer.append(TEXT_678);
    
                    }                    
                    keyCounter++;
                }
            }
            if(isEnableDebug) {
                
    stringBuffer.append(TEXT_679);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_680);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_681);
    
            }            
    		
    stringBuffer.append(TEXT_682);
    if (rejectConnName == null && useBatchSize) { // whether use batch
    		
    stringBuffer.append(TEXT_683);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_684);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_685);
    if (useBatchSize && !("").equals(batchSize) && !("0").equals(batchSize)) {
    		  
    stringBuffer.append(TEXT_686);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_687);
    
    		  }
    stringBuffer.append(TEXT_688);
    
    		}else {    		
    		
    stringBuffer.append(TEXT_689);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_690);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_691);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_692);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_693);
    
                if (("true").equals(dieOnError)) {
                    
    stringBuffer.append(TEXT_694);
    
                } else {
                    if(rejectConnName != null && rejectColumnList != null && rejectColumnList.size() > 0) {
                        
    stringBuffer.append(TEXT_695);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_696);
    stringBuffer.append(rejectConnName );
    stringBuffer.append(TEXT_697);
    
                        for(IMetadataColumn column : columnList) {
                            
    stringBuffer.append(TEXT_698);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_699);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_700);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_701);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_702);
    
                        }
                        
    stringBuffer.append(TEXT_703);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_704);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_705);
    stringBuffer.append(TEXT_706);
    stringBuffer.append(rejectConnName);
    stringBuffer.append(TEXT_707);
    stringBuffer.append(node.getUniqueName() );
    stringBuffer.append(TEXT_708);
    
                    } else {
                        
    stringBuffer.append(TEXT_709);
    
                    }
                } 
                
    stringBuffer.append(TEXT_710);
    
            } // end whether use batch
            
    stringBuffer.append(TEXT_711);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_712);
    
    	}
    	
        if(outgoingConns != null && outgoingConns.size() > 0) {
            
    stringBuffer.append(TEXT_713);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_714);
    
                for(IConnection outgoingConn : outgoingConns) {
                    if(rejectConnName == null || (rejectConnName != null && !outgoingConn.getName().equals(rejectConnName))) {
                        if(outgoingConn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {                            
                            
    stringBuffer.append(TEXT_715);
    stringBuffer.append(outgoingConn.getName());
    stringBuffer.append(TEXT_716);
    stringBuffer.append(outgoingConn.getName());
    stringBuffer.append(TEXT_717);
    
                            for(IMetadataColumn column : columnList) {
                                
    stringBuffer.append(TEXT_718);
    stringBuffer.append(outgoingConn.getName());
    stringBuffer.append(TEXT_719);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_720);
    stringBuffer.append(incomingConnName);
    stringBuffer.append(TEXT_721);
    stringBuffer.append(column.getLabel());
    stringBuffer.append(TEXT_722);
                      
                            }
                        }
                    }
                }
            
    stringBuffer.append(TEXT_723);
    
        }
        	
    	   	//////////batch execute by batch size///////
    	if (useBatchSize && (rejectConnName==null)) {
    		if(!("").equals(batchSize) && !("0").equals(batchSize)) {
    		
    stringBuffer.append(TEXT_724);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_725);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_726);
    if ( (rejectConnName==null) && (("INSERT").equals(dataAction) || ("UPDATE").equals(dataAction) || ("DELETE").equals(dataAction))) {
                
    stringBuffer.append(TEXT_727);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_728);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_729);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_730);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_731);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_732);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_733);
    if (("INSERT").equals(dataAction)) {
            	    	
    stringBuffer.append(TEXT_734);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_735);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_736);
    
            	    	}else if (("UPDATE").equals(dataAction)) {
            	    	
    stringBuffer.append(TEXT_737);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_738);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_739);
    
            	    	}else if (("DELETE").equals(dataAction)) {
            	    	
    stringBuffer.append(TEXT_740);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_741);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_742);
    
            	    	}
    stringBuffer.append(TEXT_743);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_744);
    if(("true").equals(dieOnError)) {
                	
    stringBuffer.append(TEXT_745);
    
                	}else {
                	
    stringBuffer.append(TEXT_746);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_747);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_748);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_749);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_750);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_751);
    if (("INSERT").equals(dataAction)) {
			    	
    stringBuffer.append(TEXT_752);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_753);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_754);
    
			    	}else if (("UPDATE").equals(dataAction)) {
			    	
    stringBuffer.append(TEXT_755);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_756);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_757);
    
			    	}else if (("DELETE").equals(dataAction)) {
			    	
    stringBuffer.append(TEXT_758);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_759);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_760);
    
			    	}
    stringBuffer.append(TEXT_761);
    
                	}
    stringBuffer.append(TEXT_762);
    
                }
    stringBuffer.append(TEXT_763);
    	
    		}
    	
    	}
        ////////////commit every////////////
		if(!useExistingConnection) {
    		if(!("").equals(commitEvery) && !("0").equals(commitEvery)) {
    		    
    stringBuffer.append(TEXT_764);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_765);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_766);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_767);
    if ((rejectConnName==null && useBatchSize) && (("INSERT").equals(dataAction) || ("UPDATE").equals(dataAction) || ("DELETE").equals(dataAction)) ) {
                
    stringBuffer.append(TEXT_768);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_769);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_770);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_771);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_772);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_773);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_774);
    if (("INSERT").equals(dataAction)) {
            	    	
    stringBuffer.append(TEXT_775);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_776);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_777);
    
            	    	}else if (("UPDATE").equals(dataAction)) {
            	    	
    stringBuffer.append(TEXT_778);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_779);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_780);
    
            	    	}else if (("DELETE").equals(dataAction)) {
            	    	
    stringBuffer.append(TEXT_781);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_782);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_783);
    
            	    	}
    stringBuffer.append(TEXT_784);
    if(("true").equals(dieOnError)) {
                	
    stringBuffer.append(TEXT_785);
    
                	}else {
                	
    stringBuffer.append(TEXT_786);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_787);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_788);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_789);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_790);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_791);
    if (("INSERT").equals(dataAction)) {
            	    	
    stringBuffer.append(TEXT_792);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_793);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_794);
    
            	    	}else if (("UPDATE").equals(dataAction)) {
            	    	
    stringBuffer.append(TEXT_795);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_796);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_797);
    
            	    	}else if (("DELETE").equals(dataAction)) {
            	    	
    stringBuffer.append(TEXT_798);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_799);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_800);
    
            	    	}
    stringBuffer.append(TEXT_801);
    
                	}
    stringBuffer.append(TEXT_802);
    
                }
    stringBuffer.append(TEXT_803);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_804);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_805);
    
    		}
    	}
        
        
    }
    
    stringBuffer.append(TEXT_806);
    return stringBuffer.toString();
  }
}
