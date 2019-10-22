grammar ICSS;

//--- LEXER: ---
// IF support:
IF: 'if';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---
stylesheet: (variable | stylerule)+?  | EOF;
stylerule: selector + OPEN_BRACE + (ifclause | declaration)+ CLOSE_BRACE;
variable: CAPITAL_IDENT + ASSIGNMENT_OPERATOR + expression + SEMICOLON;
declaration: LOWER_IDENT + COLON + expression + SEMICOLON;
selector: ID_IDENT | CLASS_IDENT | LOWER_IDENT;
ifclause: IF + BOX_BRACKET_OPEN + expression + BOX_BRACKET_CLOSE + OPEN_BRACE + (ifclause | declaration)+ CLOSE_BRACE;
expression: multiplyoperation | subtractoperation | addoperation | literal;
literal: CAPITAL_IDENT | COLOR | PIXELSIZE | TRUE | FALSE | SCALAR | PERCENTAGE;
multiplyoperation: literal + MUL + literal | literal + MUL + expression;
subtractoperation: literal + MIN + literal | literal + MIN + expression;
addoperation: literal + PLUS + literal | literal + PLUS + expression;