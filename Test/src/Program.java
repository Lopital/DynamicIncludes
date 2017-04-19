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
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.TokenStream;
import lexer.PreprocessorExtractor;
import static java.nio.file.FileVisitResult.*;
import java.nio.CharBuffer;

public class Program {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// AnalizeCPreprocessorsListener listener = new
		// AnalizeCPreprocessorsListener();
		// RunAntlrOnFile("d:\\Temp\\CPrep\\text.c", listener);

		Path CurrentDirPath = null;
		try {
			CurrentDirPath = getJarContainingFolder(Program.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}

		Path ConfigFilePath = CurrentDirPath.resolve("TestResources/config.txt").normalize();
		Path OutputDir = CurrentDirPath.resolve("../../../TestOutput/PP").normalize();
		Path OutputDirTest = CurrentDirPath.resolve("../../../TestOutput/PP_Test").normalize();

		try {
			Config config = getFiles(ConfigFilePath);

			if (Files.exists(OutputDir, LinkOption.NOFOLLOW_LINKS)) {
				clearDirectory(OutputDir);
			}
			if (Files.exists(OutputDirTest, LinkOption.NOFOLLOW_LINKS)) {
				clearDirectory(OutputDirTest);
			}

			for (String sourceFilePath : config.Files) {
				System.out.println(sourceFilePath);
				String destinationFullFilePath = OutputDir + sourceFilePath;
				String sourceFullFilePath = config.RootPath + sourceFilePath;
				if (false && !sourceFilePath.endsWith("\\file_name.c")) {
					continue;
				}
				try {
					extractPreprocessors(sourceFullFilePath, destinationFullFilePath);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				String destinationFullFilePathTest = OutputDirTest + sourceFilePath;
				try {
					extractPreprocessorsTest(sourceFullFilePath, destinationFullFilePathTest);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (

		FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

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

	@SuppressWarnings("unused")
	private static void runAntlrOnFile(String filePath, AnalizeCPreprocessorsListener listener) {
		CLangPreprocessorLexer lexer = new CLangPreprocessorLexer(new ANTLRInputStream(filePath));
		TokenStream tokenStream = new CommonTokenStream(lexer);
		CLangPreprocessorParser parser = new CLangPreprocessorParser(tokenStream);
		parser.setBuildParseTree(false);
		parser.addParseListener(listener);
		parser.preprocessorDirective();
	}

	private static void extractPreprocessors(String source, String destination)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		List<? extends Token> tokens = GetLexerTokensFromFile(source);
		writeTokensToFile(destination, tokens);
	}

	private static void extractPreprocessorsTest(String source, String destination)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		CharBuffer cBuffer = getCharBuffer(source);

		PreprocessorExtractor.Instance.removeComments(cBuffer);
		PreprocessorExtractor.Instance.extarctPreprocessorLines(cBuffer);

		writeCharBufferToFile(destination, cBuffer);
	}

	private static void writeCharBufferToFile(String destination, CharBuffer cBuffer)
			throws IOException, UnsupportedEncodingException, FileNotFoundException {
		File file = Paths.get(destination).toFile();
		File dir = file.getParentFile();
		dir.mkdirs();
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destination), "Cp1252"))) {
			writer.write(cBuffer.toString());
		}
	}

	private static CharBuffer getCharBuffer(String source)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		int size = (int) new File(source).length();
		CharBuffer cBuffer = CharBuffer.allocate(size);
		Reader reader = new InputStreamReader(new FileInputStream(source), "Cp1252");
		reader.read(cBuffer);
		reader.close();
		cBuffer.flip();
		return cBuffer;
	}

	private static ANTLRErrorListener listener = new LexerErrorWriter();

	private static List<? extends Token> GetLexerTokensFromFile(String filePath) {
		CLangPreprocessorLexer lexer = null;
		try {
			lexer = new CLangPreprocessorLexer(
					new ANTLRInputStream(new InputStreamReader(new FileInputStream(filePath), "Cp1252")));
			lexer.removeErrorListeners();
			lexer.addErrorListener(listener);
			List<? extends Token> tokens = lexer.getAllTokens();
			return tokens;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<Token>();
	}

	private static void writeTokensToFile(String filePath, List<? extends Token> tokens)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {

		File file = Paths.get(filePath).toFile();
		File dir = file.getParentFile();
		dir.mkdirs();
		StringBuilder sb = new StringBuilder();
		for (Token token : tokens) {
			sb.append(token.getText());
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "Cp1252"))) {
			writer.write(sb.toString());
		}
	}
}
