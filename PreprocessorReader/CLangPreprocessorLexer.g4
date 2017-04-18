lexer grammar CLangPreprocessorLexer;

//comments whitespace ignore
LINE_COMMENT_PREP 	: LINE_COMMENT -> skip ;
COMMENT_PREP 		: '/*' -> more, pushMode(Comment);
WS_PREP 			: WS -> skip ;
NL_PREP 			: NL -> skip ;

IDENT_				: IDENT -> skip ;
NUMBER				: DIGIT+ -> skip ;
TEXT				: '"' ('\\'. | ~["\\])* '"' -> skip ;

INCLUDE 			: '#include' -> pushMode(Include) ;
DEFINE 				: '#define' -> pushMode(Define) ;
UNDEF 				: '#undef' -> pushMode(MacroName) ;
IF 					: '#if' -> pushMode(Condition) ;
IFDEF 				: '#ifdef' -> pushMode(MacroName) ;
IFNDEF 				: '#ifndef' -> pushMode(MacroName) ;
ELSE 				: '#else' -> pushMode(LineEnd) ;
ELIF 				: '#elif' -> pushMode(Condition) ;
ENDIF 				: '#endif' -> pushMode(LineEnd) ;
ERROR 				: '#'IDENT? -> pushMode(IgnoreLine), skip ;


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
WS_LE 				: WS+ -> skip ;
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
WS_DEF 				: WS+ -> skip ;

NL_DEF 				: NL -> type(NL), popMode ;
IDENT_DEF 			: IDENT -> type(IDENT) ;
O_P_DEF 			: O_P -> type(O_P) ;
C_P_DEF 			: C_P -> type(C_P) ;
COMMA_DEF 			: COMMA -> type(COMMA) ;
VALUE_PART 			: ( '/' ~[*/] | '\\' NL | ~[\r\n/] )+ ;


mode MacroName;

WS_MACRO 			: WS+ -> skip ;
NL_MACRO 			: NL -> type(NL), popMode ;
NAME_MACRO 			: IDENT -> type(IDENT) ;

//comments whitespace ignore
LINE_COMMENT_MACRO 	: LINE_COMMENT -> skip ;
COMMENT_MACRO 		: '/*' -> more, pushMode(Comment) ;


mode Condition;

WS_COND 			: WS+ -> skip ;
NL_COND 			: NL -> type(NL), popMode ;
CONDITION_PART 		: ( '/' ~[*/] | ~[\r\n/] )+ ;

//comments whitespace ignore
LINE_COMMENT_COND 	: LINE_COMMENT -> skip ;
COMMENT_COND 		: '/*' -> more, pushMode(Comment) ;


mode Include;

WS_INC 				: WS+ -> skip ;
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
WS 					: [ \t]+ ;

IDENT 				: WORD_CHAR (WORD_CHAR|DIGIT)* ;
NL 					: '\r' '\n'? | '\n' ;
O_P 				: '(' ;
C_P 				: ')' ;
COMMA 				: ',' ;

fragment DIGIT		: [0-9];
fragment LETTER		: [A-Za-z];
fragment WORD_CHAR	: LETTER | '_' ;
