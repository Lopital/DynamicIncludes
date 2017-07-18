package test.preprocessor.lexer;

import java.nio.CharBuffer;

public class PreprocessorExtractor {

	public static PreprocessorExtractor Instance = new PreprocessorExtractor();

	private PreprocessorExtractor() {
	}

	@Deprecated
	public CharBuffer removeCommentsV1(CharBuffer input) {
		int length = input.length();
		CharBuffer cBuffer = CharBuffer.allocate(length);

		for (int i = 0; i < length; i++) {
			switch (input.get(i)) {
			case '/':
				i++;
				switch (input.get(i)) {
				case '*':
					i++;
					while (i < length && (input.get(i) != '*' || input.get(i + 1) != '/')) {
						i++;
					}
					i++;
					break;
				case '/':
					i++;
					while (i < length && (input.get(i) != '\r' || input.get(i) != '\n')) {
						i++;
					}
					cBuffer.append(input.get(i));
					break;
				default:
					cBuffer.append(input.get(i - 1));
					cBuffer.append(input.get(i));
					break;
				}
				break;
			default:
				cBuffer.append(input.get(i));
				break;
			}
		}
		return cBuffer;
	}

	public CharBuffer removeComments(CharBuffer input) {

		CharBuffer inputReadOnly = input.asReadOnlyBuffer();
		inputReadOnly.rewind();

		CharBuffer output = input;
		output.rewind();

		while (inputReadOnly.hasRemaining()) {
			char c = inputReadOnly.get();
			switch (c) {
			case '/':
				c = inputReadOnly.get();
				switch (c) {
				case '*':
					c = inputReadOnly.get();
					do {
						while (inputReadOnly.hasRemaining() && c != '*') {
							c = inputReadOnly.get();
						}
					} while (inputReadOnly.hasRemaining() && (c = inputReadOnly.get()) != '/');
					break;
				case '/':
					while (inputReadOnly.hasRemaining() && ((c = inputReadOnly.get()) != '\r' && c != '\n')) {
						;
					}
					if (inputReadOnly.hasRemaining()) {
						output.put(c);
					}
					break;
				default:
					output.put('/');
					output.put(c);
					break;
				}
				break;
			default:
				output.put(c);
				break;
			}
		}
		output.flip();
		return output;
	}

	public CharBuffer extarctPreprocessorLines(CharBuffer input) {

		CharBuffer inputReadOnly = input.asReadOnlyBuffer();
		inputReadOnly.rewind();

		CharBuffer output = input;
		output.rewind();

		char c = ' ';
		while (inputReadOnly.hasRemaining()) {
			// skip whitespace from start of line
			while (inputReadOnly.hasRemaining() && (c == ' ' || c == '\t')) {
				c = inputReadOnly.get();
			}

			if (inputReadOnly.hasRemaining()) {
				switch (c) {
				case '#':
					StringBuilder sBuilder = new StringBuilder();
					while (inputReadOnly.hasRemaining()) {
						c = inputReadOnly.get();
						// if (c != ' ' && c != '\t' && c != '\r' && c != '\n'){
						if (('0' <= c && c <= '9') || ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z') || c == '_') {
							sBuilder.append(c);
						} else {
							break;
						}
					}

					String keyword = sBuilder.toString();
					switch (keyword) {
					case "include":
					case "if":
					case "elif":
					case "ifdef":
					case "ifndef":
					case "else":
					case "endif":
					case "undef":
						output.put('#');
						output.put(keyword);
						if (inputReadOnly.hasRemaining()) {
							output.put(c);
						} else {
							break;
						}
						while (inputReadOnly.hasRemaining() && !(c == '\r' || c == '\n')) {
							c = inputReadOnly.get();
							output.put(c);
						}
						if (!inputReadOnly.hasRemaining()) {
							break;
						}
						if (c == '\r' && (c = inputReadOnly.get()) == '\n') {
							output.put(c);
							if (inputReadOnly.hasRemaining()) {
								c = inputReadOnly.get();
							}
						}
						break;
					case "define":
						output.put('#');
						output.put(keyword);
						char cPrev;
						do {
							output.put(c);
							cPrev = 0;
							while (inputReadOnly.hasRemaining() && !(c == '\r' || c == '\n')) {
								cPrev = c;
								c = inputReadOnly.get();
								output.put(c);
							}
							if (inputReadOnly.hasRemaining()) {
								if (c == '\r' && (c = inputReadOnly.get()) == '\n') {
									output.put(c);
									if (inputReadOnly.hasRemaining()) {
										c = inputReadOnly.get();
									}
								}
							}
						} while (cPrev == '\\');
						break;
					default:
						while (c != '\r' && c != '\n' && inputReadOnly.hasRemaining()) {
							c = inputReadOnly.get();
						}
						if (c == '\r' && inputReadOnly.hasRemaining() && (c = inputReadOnly.get()) == '\n'
								&& inputReadOnly.hasRemaining()) {
							c = inputReadOnly.get();
						}
						break;
					}
					break;
				default:
					while (c != '\r' && c != '\n' && inputReadOnly.hasRemaining()) {
						c = inputReadOnly.get();
					}
					if (c == '\r' && inputReadOnly.hasRemaining() && (c = inputReadOnly.get()) == '\n'
							&& inputReadOnly.hasRemaining()) {
						c = inputReadOnly.get();
					}
					break;
				}
			}
		}
		output.flip();
		return output;
	}
}