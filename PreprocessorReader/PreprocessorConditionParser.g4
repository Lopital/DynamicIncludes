parser grammar PreprocessorConditionParser;

@header {
    package antlr.generated;
}

options { tokenVocab=PreprocessorConditionLexer; }

expr 		: val=INTEGER												# Integer
			| val=HEX													# Hex
			| val=OCTAL													# Octal
			| DEFINED name=IDENT										# Defined
			| DEFINED '(' name=IDENT ')'								# Defined
			| name=IDENT '(' (param+=expr (',' param+=expr)*)? ')'		# MacroFunc
			| name=IDENT												# Macro
			| '(' expression=expr ')'									# Wrapper
			| sign=(ADD|SUBSTRACT) expression=expr						# UnaryPlusMinus
			| NOT expression=expr										# Not
			| BITWISE_NOT expression=expr								# BitwiseNot
			| leftExpression=expr 
				op=(MULTIPLY | DIVIDE | PERCENT) 
			  rightExpression=expr										# MultDivRem
			| leftExpression=expr 
				op=(ADD | SUBSTRACT) 
			  rightExpression=expr										# AddSub
			| leftExpression=expr 
				op=(LEFT_SHIFT | RIGHT_SHIFT) 
			  rightExpression=expr										# BitwiseShift
			| leftExpression=expr 
				op=(LOWER | LOWEREQ | GREATER | GREATEREQ) 
			  rightExpression=expr										# RelationalOp
			| leftExpression=expr 
				op=(EQUALS | NOT_EQUALS) 
			  rightExpression=expr										# RelationalEqualsOp
			| leftExpression=expr 
				op=BITWISE_AND 
			  rightExpression=expr										# BitwiseAnd
			| leftExpression=expr 
				op=BITWISE_XOR 
			  rightExpression=expr										# BitwiseXor
			| leftExpression=expr 
				op=BITWISE_OR 
			  rightExpression=expr										# BitwiseOr
			| leftExpression=expr 
				op=AND 
			  rightExpression=expr										# LogicalAnd
			| leftExpression=expr 
				op=OR 
			  rightExpression=expr										# LogicalOr
			| cond=expr '?' caseTrue=expr ':' caseFalse=expr			# TernaryCond
			;