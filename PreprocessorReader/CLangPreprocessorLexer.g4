lexer grammar CLangPreprocessorLexer;

//comments whitespace ignore
LINE_COMMENT_PREP 	: LINE_COMMENT -> skip ;
COMMENT_PREP 		: '/*' -> more, pushMode(Comment);
WSS_PREP 			: WSS -> skip ;
NL_PREP 			: NL -> skip ;

IDENT_				: IDENT -> skip ;
NUMBER				: DIGIT+ -> skip ;
TEXT				: '"' ('\\'. | ~["\\])* '"' -> skip ;

INCLUDE 			: '#' WS* 'include' -> pushMode(Include) ;
DEFINE 				: '#' WS* 'define' -> pushMode(Define) ;
UNDEF 				: '#' WS* 'undef' -> pushMode(MacroName) ;
IF 					: '#' WS* 'if' -> pushMode(Condition) ;
IFDEF 				: '#' WS* 'ifdef' -> pushMode(MacroName) ;
IFNDEF 				: '#' WS* 'ifndef' -> pushMode(MacroName) ;
ELSE 				: '#' WS* 'else' -> pushMode(LineEnd) ;
ELIF 				: '#' WS* 'elif' -> pushMode(Condition) ;
ENDIF 				: '#' WS* 'endif' -> pushMode(LineEnd) ;
ANY 				: '#' WS* IDENT? -> pushMode(IgnoreLine), skip ;


/*
COLON				: ':' -> skip ;
LBRACE				: '{' -> skip ;
RBRACE				: '}' -> skip ;
LSBRACKET			: '[' -> skip ;
RSBRACKET			: ']' -> skip ;
LBRACKET			: '(' -> skip ;
RBRACKET			: ')' -> skip ;
COMMA_				: ',' -> skip ;
SEMICOLON			: ';' -> skip ;
EQUALS				: '=' -> skip ;
LOWER				: '<' -> skip ;
GREATER				: '>' -> skip ;
NEGATION			: '!' -> skip ;
OR					: '|' -> skip ;
AND					: '&' -> skip ;
BNERATION			: '~' -> skip ;
ADD					: '+' -> skip ;
MINUS				: '-' -> skip ;
MULTIPLICATION		: '*' -> skip ;
DIVISION			: '/' -> skip ;
DOT					: '.' -> skip ;
PERCENT				: '%' -> skip ;
QUESTION			: '?' -> skip ;
POW					: '^' -> skip ;
SLASH				: '\\' -> skip ;
QUOTE				: '\'' -> skip ;
HASH				: '#' -> skip ;
*/

//unrecognized char
DUMMY : . -> skip ;


mode LineEnd;

//comments whitespace ignore
WSS_LE 				: WSS -> skip ;
LINE_COMMENT_LE 	: LINE_COMMENT -> skip ;
COMMENT_LE 			: '/*' -> more, pushMode(Comment);

NL_LE 				: NL -> type(NL), popMode ;


mode IgnoreLine;

//comments ignore
//LINE_COMMENT_IL 	: LINE_COMMENT -> skip ;
COMMENT_IL 			: '/*' -> more, pushMode(Comment) ;

NL_IL 				: NL -> skip, popMode ;
ANY_CHAR_IL 		: ~[\r\n] -> skip ;


mode Define;

//comments whitespace ignore
LINE_COMMENT_DEF 	: LINE_COMMENT -> skip ;
COMMENT_DEF 		: '/*' -> more, pushMode(Comment) ;
WSS_DEF 			: WSS -> skip ;

IDENT_DEF 			: IDENT -> type(IDENT), pushMode(DefineParamsOptional) ;


mode DefineParamsOptional;

NL_DEF_PARAM_OP 	: NL -> type(NL), popMode, popMode ;
WSS_DEF_PARAM_OP 	: WSS -> skip, pushMode(DefineValue) ;
O_P_DEF 			: O_P -> type(O_P), pushMode(DefineParams) ;


mode DefineParams;

//comments whitespace ignore
LINE_COMMENT_DEF_PAR : LINE_COMMENT -> skip ;
COMMENT_DEF_PAR 	: '/*' -> more, pushMode(Comment) ;
WSS_DEF_PARAM 		: WSS -> skip ;

NL_DEF_PARAM 		: NL -> type(NL), popMode, popMode, popMode ;
C_P_DEF 			: C_P -> type(C_P), popMode, pushMode(DefineValue) ;
COMMA_DEF_PAR 		: COMMA -> type(COMMA) ;
IDENT_DEF_PAR 		: IDENT -> type(IDENT) ;


mode DefineValue;

//comments whitespace ignore
LINE_COMMENT_DEF_VAL : LINE_COMMENT -> skip ;
COMMENT_DEF_VAL 	: '/*' -> more, pushMode(Comment) ;
WSS_DEF_VAL 		: WSS -> skip ;

NL_DEF 				: NL -> type(NL), popMode, popMode, popMode ;
VALUE_PART 			: ( '/' ~[*/] | '\\' NL | ~[\r\n/] )+ ;
SLASH 				: '/' -> type(VALUE_PART) ;


mode MacroName;

WS_MACRO 			: WSS -> skip ;
NL_MACRO 			: NL -> type(NL), popMode ;
NAME_MACRO 			: IDENT -> type(IDENT) ;

//comments whitespace ignore
LINE_COMMENT_MACRO 	: LINE_COMMENT -> skip ;
COMMENT_MACRO 		: '/*' -> more, pushMode(Comment) ;


mode Condition;

//comments whitespace ignore
LINE_COMMENT_COND 	: LINE_COMMENT -> skip ;
COMMENT_COND 		: '/*' -> more, pushMode(Comment) ;

WSS_COND 			: WSS -> skip ;
NL_COND 			: NL -> type(NL), popMode ;
CONDITION_PART 		: ( '/' ~[*/] | '\\' NL | ~[\r\n/] )+ ;
SLASH_ 				: '/' -> type(VALUE_PART) ;


mode Include;

WSS_INC 			: WSS -> skip ;
NL_INC 				: NL -> type(NL), popMode ;

MACRO_INCLUDE 		: IDENT	-> type(IDENT) ;
SYS_INCLUDE_START 	: '<' -> pushMode(Include_Path) ;
LOCAL_INCLUDE_START : '"' -> pushMode(Include_Path) ;

//comments whitespace ignore
LINE_COMMENT_INC 	: LINE_COMMENT -> skip ;
COMMENT_INC 		: '/*' -> more, pushMode(Comment) ;


mode Include_Path;

SYS_INCLUDE_END 	: '>' -> popMode ;
LOCAL_INCLUDE_END 	: '"' -> popMode ;
FILE_PATH 			: ~[>"]+ ;


mode Comment;

//usage: COMMENT 	: '/*' -> more, mode(Comment);

END_COMMENT			: '*/' -> popMode, skip ;
COMMENT_CHAR 		: ~'*'+ -> more ;
COMMENT_STAR 		: '*' -> more ;


mode CommonTokenDefinitions;

LINE_COMMENT 		: '//' (~[\r\n])* ;
WS 					: ~[!-~\r\n] ;
WSS 				: WS+ ;

IDENT 				: WORD_CHAR (WORD_CHAR|DIGIT)* ;
NL 					: '\r' '\n'? | '\n' ;
O_P 				: '(' ;
C_P 				: ')' ;
COMMA 				: ',' ;

fragment DIGIT		: [0-9];
fragment LETTER		: [A-Za-z];
fragment WORD_CHAR	: LETTER | '_' ;
