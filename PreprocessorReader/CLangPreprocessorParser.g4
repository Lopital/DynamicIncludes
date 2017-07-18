parser grammar CLangPreprocessorParser;

options { tokenVocab=CLangPreprocessorLexer; }

@header {
    package antlr.generated;
}

sourceFile : (preprocessorDirective (NL preprocessorDirective)*)? NL? ;

preprocessorDirective : 
	  includePrep
	| definePrep
	| undefPrep
	| ifPrep
	| ifdefPrep
	| ifndefPrep
	| elifPrep
	| elsePrep
	| endifPrep
	;

includePrep : INCLUDE 
	( macro=IDENT
	| SYS_INCLUDE_START systemFilePath=FILE_PATH SYS_INCLUDE_END
	| LOCAL_INCLUDE_START localFilePath=FILE_PATH LOCAL_INCLUDE_END
	)
	;

definePrep : DEFINE macro=IDENT param=parameters? valueParts+=VALUE_PART*
	;

parameters : O_P (names+=IDENT (COMMA names+=IDENT)*)? C_P
	;

undefPrep : UNDEF macro=IDENT
	;

ifPrep : IF conditionParts+=CONDITION_PART+
	;

ifdefPrep : IFDEF macro=IDENT
	;

ifndefPrep : IFNDEF macro=IDENT
	;

elifPrep : ELIF conditionParts+=CONDITION_PART*
	;
	
elsePrep : ELSE
	;

endifPrep : ENDIF
	;

