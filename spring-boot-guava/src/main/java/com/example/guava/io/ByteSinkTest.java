package com.example.guava.io;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import com.google.common.hash.Hashing;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Test;

public class ByteSinkTest {
    private final String path = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\ByteSinkTest.txt";

    @Test
    public void testByteSink() throws IOException {
        File file = new File(path);
        file.deleteOnExit();
        ByteSink byteSink = Files.asByteSink(file);
        byte[] bytes = new byte[]{0x01, 0x02};
        byteSink.write(bytes);

        byte[] expected = Files.toByteArray(file);
        assertThat(expected, is(bytes));
    }

    @Test
    public void tstFromSourceToSink() throws IOException {
        String source = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\image.png";
        String target = "E:\\ideaWorkspace\\wang-wen-jun\\google-guava\\src\\main\\resources\\io\\image_copy.png";
        File sourceFile = new File(source);
        ByteSource byteSource = Files.asByteSource(sourceFile);

        File targetFile = new File(target);
        targetFile.deleteOnExit();
        byteSource.copyTo(Files.asByteSink(targetFile));
        assertThat(targetFile.exists(), equalTo(true));

        assertThat(
                Files.asByteSource(sourceFile).hash(Hashing.sha256()).toString(),
                equalTo(Files.asByteSource(targetFile).hash(Hashing.sha256()).toString())
        );
    }
}
