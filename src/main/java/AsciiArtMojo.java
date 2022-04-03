import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.tinylog.Logger;

@Mojo(name="ascii-art", defaultPhase = LifecyclePhase.COMPILE)
public class AsciiArtMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "artName")
    String artName;

    Map<String, String> arts = new HashMap<String, String>();


    public void execute() throws MojoExecutionException, MojoFailureException {
        AsciiArtMojo instance = new AsciiArtMojo();

        arts.put("0", "coffeemug");
        arts.put("1", "computerlove");
        arts.put("2", "teafilter");

        String choosenArtWithEnding = "";

        if (artName != null) {
            choosenArtWithEnding = artName + ".txt";
            Logger.info("choosenArtWithEnding: " + choosenArtWithEnding);
        } else {
            Random random = new Random();
            int choosenArtIndex = random.nextInt(3);
            Logger.info("choosenArtIndex: " + choosenArtIndex);
            String indexStr = String.valueOf(choosenArtIndex);
            choosenArtWithEnding = arts.get(indexStr) + ".txt";
            Logger.info("choosenArtWithEnding: " + choosenArtWithEnding);
        }

        InputStream is = instance.getFileAsIOStream(choosenArtWithEnding);

        try {
            instance.printFileContent(is);
        } catch (IOException e) {
            Logger.error(e);
        }
    }

    private InputStream getFileAsIOStream(String fileName) {
        InputStream ioStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(fileName);

        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }
        return ioStream;
    }

    private void printFileContent(InputStream is) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr);)
        {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            is.close();
        }
    }
}
