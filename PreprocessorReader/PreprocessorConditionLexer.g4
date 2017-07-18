lexer grammar PreprocessorConditionLexer;

@header {
    package antlr.generated;
}

/*
 * Integer constants.
-Character constants, which are interpreted as they would be in normal code.
-Arithmetic operators for addition, subtraction, multiplication, division,
  bitwise operations, shifts, comparisons, and logical operations (&& and ||).
  The latter two obey the usual short-circuiting rules of standard C.
-Macros. All macros in the expression are expanded before actual computation of
  the expression�s value begins.
-Uses of the defined operator, which lets you check whether macros are defined
  in the middle of an �#if�.
-Identifiers that are not macros, which are all considered to be the number zero.
  This allows you to write #if MACRO instead of #ifdef MACRO, if you know that
  MACRO, when defined, will always have a nonzero value. Function-like macros
  used without their function call parentheses are also treated as zero.
- The numeric value of character constants in preprocessor expressions.
  The preprocessor and compiler interpret character constants in the same way;
  i.e. escape sequences such as �\a� are given the values they would have on the
  target machine.
  The compiler evaluates a multi-character character constant a character at a
  time, shifting the previous value left by the number of bits per target
  character, and then or-ing in the bit-pattern of the new character truncated
  to the width of a target character. The final bit-pattern is given type int,
  and is therefore signed, regardless of whether single characters are signed or
  not. If there are more characters in the constant than would fit in the target
  int the compiler issues a warning, and the excess leading characters are ignored.
  For example, 'ab' for a target with an 8-bit char would be interpreted as
  �(int) ((unsigned char) 'a' * 256 + (unsigned char) 'b')�, and '\234a' as
  �(int) ((unsigned char) '\234' * 256 + (unsigned char) 'a')�.
 */

HEX					: '0'[xX]HEXDIGIT+ ;
OCTAL				: '0' OCTALDIGIT+ ;
INTEGER				: DIGIT+ ;
DEFINED 			: 'defined' ;
IDENT 				: WORD_CHAR (WORD_CHAR|DIGIT)* ;

HASHHASH			: '##' ;
HASH				: '#' ;
COMMA				: ',' ;

LBRACKET			: '(' ;
RBRACKET			: ')' ;

QUESTION			: '?' ;
COLON				: ':' ;

LOWER				: '<' ;
LOWEREQ				: '=<' ;
GREATER				: '>' ;
GREATEREQ			: '=>' ;
EQUALS				: '==' ;
NOT_EQUALS 			: '!=' ;

NOT 				: '!' ;
AND 				: '&&' ;
OR					: '||' ;


BITWISE_NOT			: '~' ;
BITWISE_AND 		: '&' ;
BITWISE_XOR			: '^' ;
BITWISE_OR			: '|' ;

LEFT_SHIFT			: '<<' ;
RIGHT_SHIFT			: '>>' ;

ADD					: '+' ;
SUBSTRACT			: '-' ;
MULTIPLY			: '*' ;
DIVIDE				: '/' ;
PERCENT				: '%' ;

WS 					: [ \t] -> skip ;
CHARACTER			: '\'' ('\\'.| ~['\\]) '\'' ;

//error tokens
LBRACE				: '{' ;
RBRACE				: '}' ;
LSBRACKET			: '[' ;
RSBRACKET			: ']' ;
ASSIGNE 			: '=' ;
SEMICOLON			: ';' ;
DOT					: '.' ;
SLASH				: '\\' ;
MULTI_CHARACTER		: '\'' ('\\'.| ~['\\])+ '\'' ;
TEXT				: '"' ('\\'. | ~["\\])* '"' ;

//unrecognized char
DUMMY : . ;


fragment DIGIT		: [0-9];
fragment HEXDIGIT	: [0-9a-fA-F];
fragment OCTALDIGIT	: [0-7];
fragment LETTER		: [A-Za-z];
fragment WORD_CHAR	: LETTER | '_' ;
