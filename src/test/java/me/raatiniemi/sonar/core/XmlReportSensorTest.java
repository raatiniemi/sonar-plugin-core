/*
 * Copyright (c) 2018 Tobias Raatiniemi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.raatiniemi.sonar.core;

import me.raatiniemi.sonar.core.internal.FileSystemHelpers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class XmlReportSensorTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public LogTester logTester = new LogTester();

    private final Path resourcePath = Paths.get("src", "test", "resources");
    private final MapSettings settings = new MapSettings();

    private SensorContextTester sensorContext;
    private FileSystemHelpers helpers;
    private SampleXmlReportSensor sensor;

    @Before
    public void setUp() {
        sensorContext = SensorContextTester.create(temporaryFolder.getRoot());
        helpers = FileSystemHelpers.create(sensorContext);
        sensor = SampleXmlReportSensor.create(settings.asConfig());
    }

    private void createReportFile(@Nonnull String relativePath) {
        try {
            List<String> reportLines = Files.readAllLines(Paths.get(resourcePath.toString(), "report.xml"));

            Path destination = Paths.get(temporaryFolder.getRoot().getAbsolutePath(), relativePath);
            Files.createDirectories(destination.getParent());
            Files.createFile(destination);
            Files.write(destination, reportLines);
        } catch (IOException e) {
            fail(String.format("Unable to create report file: %s", e.getMessage()));
        }
    }

    @Test
    public void execute_withDefaultReportPath() {
        DefaultInputFile inputFile = helpers.createFile("basename", "objc");
        helpers.addToFileSystem(inputFile);
        createReportFile("report.xml");

        sensor.execute(sensorContext);

        assertNotNull(sensorContext.measure(inputFile.key(), CoreMetrics.COMPLEXITY_KEY));
        assertThat(logTester.logs(LoggerLevel.DEBUG))
                .contains("Found no report path for configuration key report.path.key, using default path");
    }

    @Test
    public void execute_withReportPath() {
        settings.setProperty("report.path.key", "sonar-report.xml");
        DefaultInputFile inputFile = helpers.createFile("basename", "objc");
        helpers.addToFileSystem(inputFile);
        createReportFile("sonar-report.xml");

        sensor.execute(sensorContext);

        assertNotNull(sensorContext.measure(inputFile.key(), CoreMetrics.COMPLEXITY_KEY));
        assertThat(logTester.logs(LoggerLevel.DEBUG))
                .contains("Found report path for configuration key report.path.key");
    }
}
