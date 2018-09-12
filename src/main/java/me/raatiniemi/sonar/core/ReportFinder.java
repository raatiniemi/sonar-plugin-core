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

import org.apache.tools.ant.DirectoryScanner;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ReportFinder implements ReportPatternFinder {
    private static final Logger LOGGER = Loggers.get(ReportFinder.class);

    private final File reportDirectory;

    private ReportFinder(@Nonnull File reportDirectory) {
        this.reportDirectory = reportDirectory;
    }

    @Nonnull
    public static ReportPatternFinder create(@Nonnull File reportDirectory) {
        return new ReportFinder(reportDirectory);
    }

    @Nonnull
    public Set<File> findReportsMatching(@Nonnull String pattern) {
        LOGGER.debug("Trying to find reports matching {} in {}", pattern, getReportDirectoryPath());

        return findMatching(pattern)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Nonnull
    private String getReportDirectoryPath() {
        return reportDirectory.getAbsolutePath();
    }

    @Nonnull
    public Optional<File> findReportMatching(@Nonnull String pattern) {
        LOGGER.debug("Trying to find report matching {} in {}", pattern, getReportDirectoryPath());

        return findMatching(pattern)
                .findFirst();
    }

    @Nonnull
    private Stream<File> findMatching(@Nonnull String pattern) {
        return getBasenameForMatchingFiles(pattern)
                .map(prependBaseDirectoryPath())
                .map(Path::toFile);
    }

    @Nonnull
    private Stream<String> getBasenameForMatchingFiles(@Nonnull String pattern) {
        if (!reportDirectory.exists()) {
            LOGGER.warn("Report directory do not exsists {}", getReportDirectoryPath());
            return Stream.empty();
        }

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{pattern});
        scanner.setBasedir(reportDirectory);
        scanner.scan();

        String[] basenameForFiles = scanner.getIncludedFiles();
        int numberOfFiles = basenameForFiles.length;

        if (numberOfFiles == 0) {
            LOGGER.debug("No report(s) matching {} was found in {}", pattern, getReportDirectoryPath());
            return Stream.empty();
        }

        LOGGER.debug("Found {} report(s) matching {} in {}", numberOfFiles, pattern, getReportDirectoryPath());
        return Arrays.stream(basenameForFiles);
    }

    @Nonnull
    private Function<String, Path> prependBaseDirectoryPath() {
        return filename -> Paths.get(getReportDirectoryPath(), filename);
    }
}
