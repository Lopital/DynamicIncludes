import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.runtime.CharStream;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.LexerATNSimulator;

import lexer.PreprocessorExtractor;

import static java.nio.file.FileVisitResult.*;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

/**
 * 
 */

/**
 * @author SpaiucD
 *
 */
public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// AnalizeCPreprocessorsListener listener = new
		// AnalizeCPreprocessorsListener();
		// RunAntlrOnFile("d:\\Temp\\CPrep\\text.c", listener);
		try {
			Config config = getFiles("D:\\SpaiucD\\Projects\\_Other_\\EclipseProjects\\TestResources\\config.txt");
			String OutputDir = "D:\\SpaiucD\\Projects\\_Other_\\EclipseProjects\\TestResources\\PP";
			String OutputDirTest = "D:\\SpaiucD\\Projects\\_Other_\\EclipseProjects\\TestResources\\PP_Test";

			Path dir = Paths.get(OutputDir);
			if (Files.exists(dir, LinkOption.NOFOLLOW_LINKS)) {
				clearDirectory(dir);
			}
			Path dirTest = Paths.get(OutputDirTest);
			if (Files.exists(dirTest, LinkOption.NOFOLLOW_LINKS)) {
				clearDirectory(dirTest);
			}

			for (String sourceFilePath : config.Files) {
				System.out.println(sourceFilePath);
				String destinationFilePath = OutputDir + sourceFilePath.substring(config.RootPath.length());
				// if
				// (!sourceFilePath.equals("D:\\SpaiucD\\Projects\\_Other_\\EclipseProjects\\TestResources"
				// +
				// "\\SOURCE\\MainMicro\\Application\\YSC\\YSC_MAIN\\SOURCE\\ysc_logger.c"))
				// {
				// continue;
				// }
				try {
					extractPreprocessors(sourceFilePath, destinationFilePath);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				String destinationFilePathTest = OutputDirTest + sourceFilePath.substring(config.RootPath.length());
				try {
					extractPreprocessorsTest(sourceFilePath, destinationFilePathTest);
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

	private static Config getFiles(String filePath) throws IOException {
		Config config = new Config();
		config.Files = new ArrayList<String>();

		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		int index = 0;
		while ((line = bufferedReader.readLine()) != null) {
			if (file.length() > 0) {
				if (index == 0) {
					config.RootPath = line;
				} else {
					config.Files.add(line);
				}
				index++;
			}
		}
		bufferedReader.close();
		return config;
	}

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
