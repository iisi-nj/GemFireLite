grammar CreateIndex;

options {output=template;}

@header{
package gemlite.core.internal.index.antlr;
import gemlite.core.api.index.IndexManager;
import gemlite.core.internal.domain.DomainRegistry;
import gemlite.core.internal.domain.IMapperTool;
import gemlite.core.internal.support.context.DomainMapperHelper;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
}

@lexer::header{
package gemlite.core.internal.index.antlr;
}

@members {
public String getPackageName()
{
	return IndexManager.PACKAGE_NAME;
}
}

fragment A_ :	'a' | 'A';
fragment B_ :	'b' | 'B';
fragment C_ :	'c' | 'C';
fragment D_ :	'd' | 'D';
fragment E_ :	'e' | 'E';
fragment F_ :	'f' | 'F';
fragment G_ :	'g' | 'G';
fragment H_ :	'h' | 'H';
fragment I_ :	'i' | 'I';
fragment J_ :	'j' | 'J';
fragment K_ :	'k' | 'K';
fragment L_ :	'l' | 'L';
fragment M_ :	'm' | 'M';
fragment N_ :	'n' | 'N';
fragment O_ :	'o' | 'O';
fragment P_ :	'p' | 'P';
fragment Q_ :	'q' | 'Q';
fragment R_ :	'r' | 'R';
fragment S_ :	's' | 'S';
fragment T_ :	't' | 'T';
fragment U_ :	'u' | 'U';
fragment V_ :	'v' | 'V';
fragment W_ :	'w' | 'W';
fragment X_ :	'x' | 'X';
fragment Y_ :	'y' | 'Y';
fragment Z_ :	'z' | 'Z';

CREATE	: C_ R_ E_ A_ T_ E_;
INDEX	: I_ N_ D_ E_ X_;
HASHINDEX : H_ A_ S_ H_ I_ N_ D_ E_ X_;	
RANGEINDEX : R_ A_ N_ G_ E_ I_ N_ D_ E_ X_;	
ON	: O_ N_;
FROM	: F_ R_ O_ M_;	
TO	: T_ O_;
USING	: U_ S_ I_ N_ G_;
ORDER 	: O_ R_ D_ E_ R_;
BY	: B_ Y_;
ASC	: A_ S_ C_;
DESC	: D_ E_ S_ C_;
	

LPAREN	: '(';
RPAREN	: ')';	
DOT	: '.';	
COMMA	: ',';	
COLON	: ':';

INT :	('+'|'-')? ('0'..'9')+
;
	
ID  :	(('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*)
;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
;

keyword	returns [String value]
	: CREATE {$value = $CREATE.text;}
	|INDEX {$value = $INDEX.text;}
	|ON {$value = $ON.text;}
	|USING {$value = $USING.text;}
	|ORDER {$value = $ORDER.text;}
	|BY {$value = $BY.text;}
	|ASC {$value = $ASC.text;}
	|DESC {$value = $DESC.text;}
;	

identifier returns [String value]
	: ID {$value=$ID.text;} 
	| s1=keyword {$value = $s1.value;}
;	
package_name returns [String packageName]
	     :  s1=identifier {$packageName = $s1.value; } 
		(
		DOT {$packageName += "."; }
		s2=identifier  {$packageName += $s2.value; }
		)*
;	

class_name  returns [String className]
	    : s1=identifier
	    {
	    	$className = $s1.value;
	    }
;

method_name returns [String methodName]
	    : s1=identifier
	    {
	    	$methodName = $s1.value;
	    }
;	

index_name returns [String indexName, String indexOrder]
@after	   {
		if($indexOrder == null)
		  $indexOrder = "0"; 
           }
	   : s1=identifier 
	   {
	   	$indexName = $s1.value;	
	   }
	   (COLON INT)?
	   {
	   	$indexOrder = $INT.text;
	   }
;	

region_name returns [String regionName] 
	   : s1=identifier
	   {
	   	$regionName = $s1.value;
	   }
;

user_define_function returns [String funcName, List paramList, String lastParam, List oriParamList, String oriLastParam] 
		: s1=function_name LPAREN s2=call_parameters RPAREN
		{
		   $funcName = $s1.funcName;
		   $paramList = $s2.paramList;
		   $lastParam = $s2.lastParam; 	
		   $oriParamList = $s2.oriParamList;
		   $oriLastParam = $s2.oriLastParam;
		}
;

function_name returns [String funcName]
		: s1=package_name DOT s2=class_name DOT s3=method_name
		{
		   $funcName = $s1.packageName + "." + $s2.className + "." + $s3.methodName;	
		}
;

call_parameters returns [List paramList, String lastParam, List oriParamList, String oriLastParam]
@after 		{
		  $lastParam = (String)$paramList.get($paramList.size() - 1);
		  $paramList.remove($paramList.size() - 1);
		  
		  $oriLastParam = (String)$oriParamList.get($oriParamList.size() - 1);
		  $oriParamList.remove($oriParamList.size() - 1);
		} 
		: s1=identifier
		{
		  $paramList=new ArrayList<String>(); 
		  StringBuffer sb = new StringBuffer($s1.value);
		  sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		  $paramList.add(sb.toString());
		  
		  $oriParamList=new ArrayList<String>(); 
		  $oriParamList.add($s1.value);
		 }
		 (
		  COMMA
		  s2=identifier 
		  {
		   StringBuffer sb2 = new StringBuffer($s2.value);
		   sb2.setCharAt(0, Character.toUpperCase(sb2.charAt(0)));
		   $paramList.add(sb2.toString()); 
		   
		   $oriParamList.add($s2.value); 
		  }
		 )*
;	

attributes_list returns [List attrList, String lastParam, String udfName, List oriParamList, String oriLastParam]
		: s1=call_parameters { $attrList = $s1.paramList; $lastParam = $s1.lastParam; $oriParamList=$s1.oriParamList; $oriLastParam=$s1.oriLastParam;}
		 | s2=user_define_function { $attrList = $s2.paramList; $lastParam = $s2.lastParam;$udfName = $s2.funcName; $oriParamList=$s2.oriParamList; $oriLastParam=$s2.oriLastParam;}	
;

order_by_clause returns [Map value]
		: s1=identifier 
		  {
	            StringBuffer sb = new StringBuffer($s1.value);
		    sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		    
		    $value = new HashMap();
		    $value.put("funcName", null);
	  	    $value.put("paramList", null);
	  	    $value.put("lastParam", sb.toString());
	  	    $value.put("oriParamList", null);
	  	    $value.put("oriLastParam", $s1.value); 
	  	    $value.put("sort", true);
		  }
		  ( ASC | DESC {$value.put("sort", false);} )?
	  	  |
	  	  t1= user_define_function 
	  	  {
		    $value = new HashMap();
	  	    $value.put("funcName", $t1.funcName);
	  	    $value.put("paramList", $t1.paramList);
	  	    $value.put("lastParam", $t1.lastParam);
	  	    $value.put("oriParamList", $t1.oriParamList);
	  	    $value.put("oriLastParam", $t1.oriLastParam);
	  	    $value.put("sort", true);
	  	  }
	  	  ( ASC | DESC {$value.put("sort",false);})?
;

multi_order_by_clause  returns [List itemList, Set oriParamList]
		: ORDER BY t1=order_by_clause
		{
		  $itemList=new ArrayList(); 
		  $itemList.add($t1.value);
		  
		  $oriParamList=new HashSet<String>(); 
		  Map item = $t1.value;
		  List list = (List)item.get("oriParamList");
		  if(list != null)
		    $oriParamList.addAll(list);
		  $oriParamList.add(item.get("oriLastParam"));
	  	}
		(COMMA
		t2=order_by_clause 
		{
	          $itemList.add($t2.value);
		  Map item2 = $t2.value;
		  List list2 = (List)item2.get("oriParamList");
		  if(list2 != null)
		    $oriParamList.addAll(list2);
		  $oriParamList.add(item2.get("oriLastParam"));
		}
		)*			
;

create_rangeindex_clause returns [String indexName, String indexOrder, String packageName, String regionName,
                                  String keyClass, String valueClass, String attr, String oriAttr,
                                  List itemList, boolean keyOnly]
	: CREATE RANGEINDEX s1=index_name ON s2=region_name USING s3=identifier s4=multi_order_by_clause?
	{
		$indexName = $s1.indexName;	
		$indexOrder = $s1.indexOrder;
		$packageName = getPackageName();
		$regionName = $s2.regionName;
		DomainMapperHelper.scanMapperRegistryClass();
	   	IMapperTool tool = DomainRegistry.getMapperTool($s2.regionName);
	   	$keyClass = tool.getKeyClass().getName();
	   	$valueClass = tool.getValueClass().getName();
	   	Collection<String> kfs = tool.getKeyFieldNames();
	   	$attr = $s3.value;
	   	StringBuffer sb = new StringBuffer($s3.value);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		$oriAttr = sb.toString();
		$itemList = $s4.itemList;
		if($s4.oriParamList!=null && kfs.containsAll($s4.oriParamList))
		    $keyOnly = true;
		else
		    $keyOnly = false; 
	}
	->rangeKeyFile(packageName={getPackageName()}, indexName={$s1.indexName}, attr={$s3.value})
;

produce_rangeindex_code returns [String indexName, String packageName]
		: s1=create_rangeindex_clause
		{
		  $indexName = $s1.indexName;
		  $packageName = $s1.packageName;
		}
		->rangeClassFile(packageName={$s1.packageName}, indexName={$s1.indexName}, indexOrder={$s1.indexOrder},
			     regionName={$s1.regionName}, keyClass={$s1.keyClass}, valueClass={$s1.valueClass},
  		             attr={$s1.attr}, oriAttr={$s1.oriAttr},
  		             orderItemList={$s1.itemList}, keyOnly={$s1.keyOnly}
  		             )	
 ;

create_index_clause returns [String indexName, String indexOrder, String packageName, String regionName,
			     String keyClass, String valueClass, List attrList,
			     String lastParam, String udfName, List oriParamList, String oriLastParam, 
			     List itemList, boolean keyOnly] 
		: CREATE INDEX s1=index_name ON s2=region_name USING s3=attributes_list s4=multi_order_by_clause?
		{
		  $indexName = $s1.indexName;	
		  $indexOrder = $s1.indexOrder;
	   	  $packageName = getPackageName();
	   	  $regionName = $s2.regionName;
	   	  DomainMapperHelper.scanMapperRegistryClass();
	   	  IMapperTool tool = DomainRegistry.getMapperTool($s2.regionName);
	   	  $keyClass = tool.getKeyClass().getName();
	   	  $valueClass = tool.getValueClass().getName();
	   	  Collection<String> kfs = tool.getKeyFieldNames();
	   	  $attrList = $s3.attrList;
	   	  $lastParam = $s3.lastParam;
	   	  $udfName = $s3.udfName;
	   	  $oriParamList = $s3.oriParamList;
	   	  $oriLastParam = $s3.oriLastParam;
	   	  $itemList = $s4.itemList;
		  if($s4.oriParamList!=null && kfs.containsAll($s4.oriParamList))
		    $keyOnly = true;
		  else
		    $keyOnly = false;  
		}
		-> keyFile(packageName={getPackageName()}, indexName={$s1.indexName}, 
			   attrList={$s3.oriParamList}, lastParam={$s3.oriLastParam}, udfName={$s3.udfName})
;			

produce_index_code returns [String indexName, String packageName]
		: s1=create_index_clause
		{
		  $indexName = $s1.indexName;
		  $packageName = $s1.packageName;
		}
		-> classFile(packageName={$s1.packageName}, indexName={$s1.indexName}, indexOrder={$s1.indexOrder},
			     regionName={$s1.regionName}, keyClass={$s1.keyClass}, valueClass={$s1.valueClass},
  		             attrList={$s1.attrList}, lastParam={$s1.lastParam}, udfName={$s1.udfName}, oriParamList={$s1.oriParamList}, oriLastParam={$s1.oriLastParam},
  		             orderItemList={$s1.itemList}, keyOnly={$s1.keyOnly}
  		             )	
 ;
 
produce_test_index_code returns [String indexName, String packageName] 
		: s1=create_index_clause
		{
		  $indexName = $s1.indexName;
		  $packageName = $s1.packageName;
		}
		-> testClassFile(packageName={$s1.packageName}, indexName={$s1.indexName}, indexOrder={$s1.indexOrder},
			     regionName={$s1.regionName}, keyClass={$s1.keyClass}, valueClass={$s1.valueClass},
  		             attrList={$s1.attrList}, lastParam={$s1.lastParam}, udfName={$s1.udfName}, oriParamList={$s1.oriParamList}, oriLastParam={$s1.oriLastParam},
  		             orderItemList={$s1.itemList}, keyOnly={$s1.keyOnly}
  		             )	
;

create_mappingregion_clause returns [String indexName, String indexOrder, String packageName, String regionName,
			     String keyClass, String valueClass, List oriNameList, List nameList, 
			     List fromAttrList, String fromLastParam, List fromOriParamList, String fromOriLastParam, 
			     List toAttrList, String toLastParam, List toOriParamList, String toOriLastParam] 
	: CREATE HASHINDEX s1=index_name ON s2=region_name FROM s3=attributes_list TO s4=attributes_list
	{
		$indexName = $s1.indexName;	
		$indexOrder = $s1.indexOrder;
	   	$packageName = getPackageName();
	   	$regionName = $s2.regionName;
	   	DomainMapperHelper.scanMapperRegistryClass();
	   	IMapperTool tool = DomainRegistry.getMapperTool($s2.regionName);
	   	$keyClass = tool.getKeyClass().getName();
	   	$valueClass = tool.getValueClass().getName();
	   	Collection<String> set = tool.getKeyFieldNames();
		$oriNameList = new ArrayList<String>();
		$nameList = new ArrayList<String>();
		for(Iterator<String> it=set.iterator(); it.hasNext();)
		{
			String name = it.next();
			$oriNameList.add(name);
			StringBuffer sb = new StringBuffer(name);
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			$nameList.add(sb.toString());
		}
	   		   	
		$fromAttrList = $s3.attrList;
	   	$fromLastParam = $s3.lastParam;
	   	$fromOriParamList = $s3.oriParamList;
	   	$fromOriLastParam = $s3.oriLastParam;
	   	$toAttrList = $s4.attrList;
	   	$toLastParam = $s4.lastParam;
	   	$toOriParamList = $s4.oriParamList;
	   	$toOriLastParam = $s4.oriLastParam;
	}
	
;

produce_mappingregion_code returns [String indexName, String packageName]
	: s1=create_mappingregion_clause
	{
		$indexName = $s1.indexName;
		$packageName = $s1.packageName;
	}
	-> mappingRegionClassFile(packageName={$s1.packageName}, indexName={$s1.indexName}, indexOrder={$s1.indexOrder},
			     	  regionName={$s1.regionName}, keyClass={$s1.keyClass}, valueClass={$s1.valueClass}, oriNameList={$s1.oriNameList}, nameList={$s1.nameList},
  		                  fromAttrList={$s1.fromAttrList}, fromLastParam={$s1.fromLastParam}, fromOriParamList={$s1.fromOriParamList}, fromOriLastParam={$s1.fromOriLastParam},
  		                  toAttrList={$s1.toAttrList}, toLastParam={$s1.toLastParam}, toOriParamList={$s1.toOriParamList}, toOriLastParam={$s1.toOriLastParam})
;


produce_mappinglocalcache_code returns [String indexName, String packageName]
	: s1=create_mappingregion_clause
	{
		$indexName = $s1.indexName;
		$packageName = $s1.packageName;
	}
	-> mappingLocalCacheClassFile(packageName={$s1.packageName}, indexName={$s1.indexName}, fromOriParamList={$s1.fromOriParamList}, fromOriLastParam={$s1.fromOriLastParam})
;

produce_mappingRegionResolver_code returns [String indexName, String packageName]
	: s1=create_mappingregion_clause
	{
		$indexName = $s1.indexName;
		$packageName = $s1.packageName;
	}
	-> mappingRegionResolverClassFile(packageName={$s1.packageName}, indexName={$s1.indexName}, fromOriParamList={$s1.fromOriParamList}, fromOriLastParam={$s1.fromOriLastParam})
;