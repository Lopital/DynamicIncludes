parser grammar CLangPreprocessorParser;

options { tokenVocab=CLangPreprocessorLexer; }

sourceFile : preprocessorDirective* ;

preprocessorDirective : 
	( includePrep
	| definePrep
	| undefPrep
	| ifPrep
	| ifdefPrep
	| ifndefPrep
	| elifPrep
	| elsePrep
	| endifPrep
	) NL
	;

includePrep : keyword=INCLUDE 
	( macro=IDENT
	| SYS_INCLUDE_START systemFilePath=FILE_PATH SYS_INCLUDE_END
	| LOCAL_INCLUDE_START localFilePath=FILE_PATH LOCAL_INCLUDE_END
	)
	;

definePrep : keyword=DEFINE macro=IDENT param=parameters? valueParts+=VALUE_PART*
	;

parameters : O_P (names+=IDENT (COMMA names+=IDENT)*)? C_P
	;

undefPrep : keyword=UNDEF macro=IDENT
	;

ifPrep : keyword=IF conditionParts+=CONDITION_PART
	;

ifdefPrep : keyword=IFDEF macro=IDENT
	;

ifndefPrep : keyword=IFNDEF macro=IDENT
	;

elifPrep : keyword=ELIF conditionParts+=CONDITION_PART
	;
	
elsePrep : keyword=ELSE
	;

endifPrep : keyword=ENDIF
	;

