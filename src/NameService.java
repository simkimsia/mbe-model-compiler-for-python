
public class NameService {
	// General description
	// Model compiler
	// This is a mechanism class that provides some name conditioning services to
	// the model compiler
	// Invariants
	// none
	// Implementation notes
	// it seems really inefficient to do this all in immutable String, but an
	// attempt to use StringBuffer instead
	// led to all kinds of weird characters being injected before converted names.
	// And that led to all kinds of
	// trouble with the code in the IDE. It could not even compile, let alone
	// execute.
	// Application notes
	// none
	//
	// This material is copied and/or adapted from the (to be)
	// How to Engineer Software website
	//
	// http://www.Construx.com/howtoengineersw
	//
	// This material is Copyright � 2018 by Stephen R. Tockey
	// Permission is hereby given to copy, adapt, and distribute this material as
	// long as this notice is included on all such materials and the materials are
	// not sold, licensed, or otherwise distributed for commercial gain.
	//
	// This tool is being distributed "as-is". No warrantee is expressed or implied.
	// While the author believes that this tool gives correct answers, the user
	// assumes all risk of use.

	// Constants

	public static String tagDescriptionStart = "<description>";
	// public static String tagDescriptionEnd = "</description>";

	// Static (class) variables

	// none

	// Static (class) methods

	public static String asClassLevelName(String originalName) {
		// description
		// formats originalName to match Java style for class-level names
		// removes non-alpha and non-numeric characters
		// makes camelCase, capitalizing after any non-alphanumeric
		// first letter is capitalized
		// requires
		// originalName <> null
		// guarantees
		// returns a re-formatted originalName, as a String
		String formattedString = new String();
		boolean wasValid = false;
		for (int charIndex = 0; charIndex < originalName.length(); charIndex++) {
			Character theChar = originalName.charAt(charIndex);
			if (Character.isLetter(theChar) || Character.isDigit(theChar)) {
				if (wasValid) {
					formattedString = formattedString + Character.toLowerCase(theChar);
					wasValid = true;
				} else {
					formattedString = formattedString + Character.toUpperCase(theChar);
					wasValid = true;
				}
			} else {
				wasValid = false;
			}
		}
		return formattedString;
	}

	// specific to python we want snake_style for method of attribute
	public static String asSnakeStyleName(String originalName) {
		// description
		// formats originalName to match Java style for class-level names
		// removes non-alpha and non-numeric characters
		// makes camelCase, capitalizing after any non-alphanumeric
		// first letter is capitalized
		// requires
		// originalName <> null
		// guarantees
		// returns a re-formatted originalName, as a String
		originalName = originalName.trim();
		int n = originalName.length();
		String formattedString = "";

		for (int i = 0; i < n; i++) {
			// Converting space
			// to underscor
			if (originalName.charAt(i) == ' ')
				formattedString = formattedString + '_';
			else

				// If not space, convert
				// into lower character
				formattedString = formattedString + Character.toLowerCase(originalName.charAt(i));
		}

		return formattedString;
	}

	public static String asInstanceLevelName(String originalName) {
		// description
		// formats originalName to match Java style for instance-level names
		// removes non-alpha and non-numeric characters
		// makes camelCase, capitalizing after any non-alphanumeric
		// first letter is always lower case
		// requires
		// originalName <> null
		// Context.mMClass() <> null
		// guarantees
		// if there is at least one letter in originalName
		// then returns the re-formatted originalName
		// otherwise reports an error and returns an empty string
		Character theChar;
		String formattedString = new String();
		int charIndex = 0;
		theChar = originalName.charAt(charIndex);
		while (!Character.isLetter(theChar) && charIndex < originalName.length()) {
			charIndex++;
			theChar = originalName.charAt(charIndex);
		}
		if (charIndex != originalName.length()) {
			formattedString = formattedString + Character.toLowerCase(theChar);
			charIndex++;
			boolean wasValid = true;
			while (charIndex < originalName.length()) {
				theChar = originalName.charAt(charIndex);
				if (Character.isLetter(theChar) || Character.isDigit(theChar)) {
					if (wasValid) {
						formattedString = formattedString + Character.toLowerCase(theChar);
						wasValid = true;
					} else {
						formattedString = formattedString + Character.toUpperCase(theChar);
						wasValid = true;
					}
				} else {
					wasValid = false;
				}
				charIndex++;
			}
		} else {
			System.out.println();
			System.out.println("****** ERROR in name! ******");
			System.out.println("Class = " + Context.mMClass().name() + ", name = " + originalName);
			System.out.println();
			Context.codeOutput().incrementErrorCount();
		}
		return formattedString;
	}

	public static String asUPPERCASE(String originalName) {
		// description
		// formats originalName to match Java style for ENUM names
		// removes non-alpha and non-numeric characters
		// makes everything UPPER CASE
		// requires
		// originalName <> null
		// guarantees
		// returns a re-formatted originalName, as a String
		String formattedString = new String();
		for (int charIndex = 0; charIndex < originalName.length(); charIndex++) {
			Character theChar = originalName.charAt(charIndex);
			if (Character.isLetter(theChar) || Character.isDigit(theChar)) {
				formattedString = formattedString + Character.toUpperCase(theChar);
			}
		}
		return formattedString;
	}

	public static String formatActionStmt(String originalString) {
		// description
		// formats string originalLine to convert non-Java JAL action action language
		// components
		// into something acceptable to Java:
		// 1) turns text between @s and @ into quoted text (to support strings and
		// sys.out.print() )
		// 2) turns text between @i and @ into an instance level name
		// 3) turns text between @c and @ into a class level name
		// 4) turns text between @e and @ into an enum name
		// 5) turns text between @a and @ into an private instance attribute e.g.
		// `self._<the_attribute>`
		// requires
		// originalString <> null
		// Context.mMClass() <> null
		// guarantees
		// returns a re-formatted originalString, as a String
		Character theChar;
		String formattedString = new String();
		int atSignCount = 0;
		for (int charIndex = 0; charIndex < originalString.length(); charIndex++) {
			theChar = originalString.charAt(charIndex);
			if (theChar == '@') {
				atSignCount++;
			}
		}
		if ((atSignCount % 2) == 0) {
			String temporaryString;
			int charIndex = 0;
			boolean formattingFailed = false;
			while (charIndex < originalString.length() && !formattingFailed) {
				theChar = originalString.charAt(charIndex);
				if (theChar != '@') {
					formattedString = formattedString + theChar;
					charIndex++;
				} else {
					charIndex++;
					theChar = originalString.charAt(charIndex);
					switch (theChar) {
						case 's':
							formattedString = formattedString + "\"";
							charIndex++;
							theChar = originalString.charAt(charIndex);
							while (theChar != '@') {
								formattedString = formattedString + theChar;
								charIndex++;
								theChar = originalString.charAt(charIndex);
							}
							formattedString = formattedString + "\"";
							break;
						case 'a':
							temporaryString = new String();
							charIndex++;
							theChar = originalString.charAt(charIndex);
							while (theChar != '@') {
								temporaryString = temporaryString + theChar;
								charIndex++;
								theChar = originalString.charAt(charIndex);
							}
							// @NOTE because we assume all instance attributes are private so we prepend
							// with self._
							formattedString = "self._" + formattedString
									+ NameService.asSnakeStyleName(temporaryString);
							break;
						case 'i':
							temporaryString = new String();
							charIndex++;
							theChar = originalString.charAt(charIndex);
							while (theChar != '@') {
								temporaryString = temporaryString + theChar;
								charIndex++;
								theChar = originalString.charAt(charIndex);
							}
							// @NOTE because we assume all instance attributes are private so we prepend
							// with self._
							formattedString = formattedString + NameService.asSnakeStyleName(temporaryString);
							break;
						case 'c':
							temporaryString = new String();
							charIndex++;
							theChar = originalString.charAt(charIndex);
							while (theChar != '@') {
								temporaryString = temporaryString + theChar;
								charIndex++;
								theChar = originalString.charAt(charIndex);
							}
							formattedString = formattedString + NameService.asClassLevelName(temporaryString);
							break;
						case 'e':
							temporaryString = new String();
							charIndex++;
							theChar = originalString.charAt(charIndex);
							while (theChar != '@') {
								temporaryString = temporaryString + theChar;
								charIndex++;
								theChar = originalString.charAt(charIndex);
							}
							formattedString = formattedString + NameService.asUPPERCASE(temporaryString);
							break;
						default:
							System.out.println();
							System.out.println(
									"****** ERROR in action language statement! '@' followed by inappropriate char ******");
							System.out
									.println("Class = " + Context.mMClass().name() + ", statement: " + originalString);
							System.out.println();
							Context.codeOutput().incrementErrorCount();
							formattingFailed = true;
					}
					charIndex++;
				}
			}
		} else {
			System.out.println();
			System.out.println("****** ERROR in action language statement! Unbalanced '@' in statement ******");
			System.out.println("Class = " + Context.mMClass().name() + ", statement: " + originalString);
			System.out.println();
			Context.codeOutput().incrementErrorCount();
		}
		return formattedString;
	}

	// Instance variables

	// none

	// Constructor(s)

	// none

	// Accessors

	// none

	// Modifiers

	// none

	// Private methods

	// none

}
