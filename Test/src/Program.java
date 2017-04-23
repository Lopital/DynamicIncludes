import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import lexer.PreprocessorExtractor;
import static java.nio.file.FileVisitResult.*;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class Program {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		Path CurrentDirPath = null;
		try {
			CurrentDirPath = getJarContainingFolder(Program.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}

		Charset charsetCp1252 = Charset.forName("Cp1252");
		Path configFilePath = CurrentDirPath.resolve("TestResources/config.txt").normalize();
		Path outputDirLexer = CurrentDirPath.resolve("../../../TestOutput/PP_Lexer").normalize();
		Path outputDirParser = CurrentDirPath.resolve("../../../TestOutput/PP_Parser").normalize();
		Path outputDirTest = CurrentDirPath.resolve("../../../TestOutput/PP_Test").normalize();

		try {
			Config config = getFiles(configFilePath);

			if (Files.exists(outputDirLexer, LinkOption.NOFOLLOW_LINKS)) {
				clearDirectory(outputDirLexer);
			}
			if (Files.exists(outputDirParser, LinkOption.NOFOLLOW_LINKS)) {
				clearDirectory(outputDirParser);
			}
			if (Files.exists(outputDirTest, LinkOption.NOFOLLOW_LINKS)) {
				clearDirectory(outputDirTest);
			}

			for (String sourceFilePath : config.Files) {
				System.out.println(sourceFilePath);
				String destinationLexerFullFilePath = outputDirLexer + sourceFilePath;
				String destinationParserFullFilePath = outputDirParser + sourceFilePath;
				String destinationTestFullFilePath = outputDirTest + sourceFilePath;
				String sourceFullFilePath = config.RootPath + sourceFilePath;

				// debug condition
				if (false && !sourceFilePath.endsWith("\\file_name.c")) {
					continue;
				}

				try {
					extractPreprocessorsLexer(sourceFullFilePath, destinationLexerFullFilePath, charsetCp1252);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					extractPreprocessorsParser(sourceFullFilePath, destinationParserFullFilePath, charsetCp1252);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					extractPreprocessorsTest(sourceFullFilePath, destinationTestFullFilePath, charsetCp1252);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

	}

	// Utils
	public static Path getJarContainingFolder(Class<?> aclass) throws Exception {
		CodeSource codeSource = aclass.getProtectionDomain().getCodeSource();

		Path jarFile;

		if (codeSource.getLocation() != null) {

			jarFile = Paths.get(codeSource.getLocation().toURI());
		} else {
			String path = aclass.getResource(aclass.getSimpleName() + ".class").getPath();
			String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
			jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
			jarFile = Paths.get(jarFilePath);
		}
		return jarFile.getParent();
	}

	public static void clearDirectory(final Path path) throws IOException {
		if (path.toFile().exists()) {
			for (File subPath : path.toFile().listFiles()) {
				Files.walkFileTree(subPath.toPath(), new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
							throws IOException {
						Files.delete(file);
						return CONTINUE;
					}

					@Override
					public FileVisitResult visitFileFailed(final Path file, final IOException e) {
						return handleException(e);
					}

					private FileVisitResult handleException(final IOException e) {
						e.printStackTrace(); // replace with more robust
												// error
												// handling
						return TERMINATE;
					}

					@Override
					public FileVisitResult postVisitDirectory(final Path dir, final IOException e) throws IOException {
						if (e != null)
							return handleException(e);
						Files.delete(dir);
						return CONTINUE;
					}
				});
			}
		}
	}

	private static Config getFiles(Path filePath) throws IOException {
		Config config = new Config();
		config.Files = new ArrayList<String>();

		File file = filePath.toFile();
		FileReader fileReader = new FileReader(file);
		config.RootPath = filePath.getParent().toString();
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			if (line.length() > 0) {
				config.Files.add(line);
			}
		}
		bufferedReader.close();
		return config;
	}

	private static String removeWhitespaces(String text) {
		return text.replaceAll("\\s", "");
	}

	// Test
	private static void extractPreprocessorsTest(String source, String destination, Charset charset)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		CharBuffer cBuffer = getCharBuffer(source, charset);

		PreprocessorExtractor.Instance.removeComments(cBuffer);
		PreprocessorExtractor.Instance.extarctPreprocessorLines(cBuffer);

		writeCharBufferToFile(destination, cBuffer, charset);
	}

	private static void writeCharBufferToFile(String destination, CharBuffer cBuffer, Charset charset)
			throws IOException, UnsupportedEncodingException, FileNotFoundException {
		File file = Paths.get(destination).toFile();
		File dir = file.getParentFile();
		dir.mkdirs();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destination), charset))) {
			writer.write(removeWhitespaces(cBuffer.toString()));
		}
	}

	private static CharBuffer getCharBuffer(String source, Charset charset)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		int size = (int) new File(source).length();
		CharBuffer cBuffer = CharBuffer.allocate(size);
		Reader reader = new InputStreamReader(new FileInputStream(source), charset);
		reader.read(cBuffer);
		reader.close();
		cBuffer.flip();
		return cBuffer;
	}

	// Lexer
	private static void extractPreprocessorsLexer(String source, String destination, Charset charset)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		List<? extends Token> tokens = GetLexerTokensFromFile(source, charset);
		writeTokensToFile(destination, tokens, charset);
	}

	private static ANTLRErrorListener listener = new LexerErrorWriter();

	private static List<? extends Token> GetLexerTokensFromFile(String filePath, Charset charset) {
		CLangPreprocessorLexer lexer = null;
		try {
			lexer = new CLangPreprocessorLexer(
					new ANTLRInputStream(new InputStreamReader(new FileInputStream(filePath), charset)));
			lexer.removeErrorListeners();
			lexer.addErrorListener(listener);
			List<? extends Token> tokens = lexer.getAllTokens();
			return tokens;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<Token>();
	}

	private static void writeTokensToFile(String filePath, List<? extends Token> tokens, Charset charset)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {

		File file = Paths.get(filePath).toFile();
		File dir = file.getParentFile();
		dir.mkdirs();
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens) {
			sb.append(token.getText());
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), charset))) {
			writer.write(removeWhitespaces(sb.toString()));
		}
	}

	// Parser
	private static GrammarManager manager = new GrammarManager();

	private static void extractPreprocessorsParser(String sourceFullFilePath, String destinationParserFullFilePath,
			Charset charset) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		SourceFilePreprocessorDirectives fileSource = manager.extractPreprocessor(Paths.get(sourceFullFilePath),
				charset);
		PreprocessorDBWriterTest visitor = new PreprocessorDBWriterTest();
		visitor.init();
		fileSource.accept(visitor);
		String text = visitor.getSb().toString();

		writeTextToFile(destinationParserFullFilePath, text, charset);
	}

	private static void writeTextToFile(String filePath, String text, Charset charset)
			throws IOException, UnsupportedEncodingException, FileNotFoundException {
		File file = Paths.get(filePath).toFile();
		File dir = file.getParentFile();
		dir.mkdirs();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), charset))) {
			writer.write(removeWhitespaces(text));
		}
	}

}
