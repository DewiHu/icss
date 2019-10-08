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
LOWER_IDENT: [a-z0-9\-]+;
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
stylesheet: property+;
property: variable | element;
variable: CAPITAL_IDENT + ASSIGNMENT_OPERATOR + expression + SEMICOLON;
element: selector + OPEN_BRACE + declarations + CLOSE_BRACE;
selector: ID_IDENT | CLASS_IDENT | LOWER_IDENT;
declarations: declaration+;
declaration: LOWER_IDENT + COLON + expression + SEMICOLON | if_expression;
if_expression: IF + BOX_BRACKET_OPEN + expression + BOX_BRACKET_CLOSE + OPEN_BRACE + declarations + CLOSE_BRACE;
expression: value | operations;
operations: operation+;
operation:  operator + value;
operator: PLUS | MIN | MUL;
value: COLOR | PIXELSIZE | CAPITAL_IDENT | TRUE | FALSE | SCALAR;
