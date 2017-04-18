parser grammar CLangPreprocessorParser;

options { tokenVocab=CLangPreprocessorLexer; }

preprocessorDirective : 
	( includePrep
	| definePrep
	| undefPrep
	| ifPrep
	| elifPrep
	| ifdefPrep
	| ifndefPrep
	| elsePrep
	| endifPrep
	) NL
	;

includePrep : INCLUDE 
	( macro=IDENT
	| SYS_INCLUDE_START systemFilePath=FILE_PATH SYS_INCLUDE_END
	| LOCAL_INCLUDE_START localFilePath=FILE_PATH LOCAL_INCLUDE_END
	)
	;

definePrep : DEFINE macro=IDENT params=parameters? valueParts+=VALUE_PART*
	;

parameters : O_P (param+=IDENT (COMMA param+=IDENT)*)? C_P
	;

undefPrep : UNDEF macro=IDENT
	;

ifPrep : IF conditionParts+=CONDITION_PART
	;

elifPrep : ELIF conditionParts+=CONDITION_PART
	;
	
ifdefPrep : IFDEF macro=IDENT
	;

ifndefPrep : IFNDEF macro=IDENT
	;

elsePrep : ELSE
	;

endifPrep : ENDIF
	;

