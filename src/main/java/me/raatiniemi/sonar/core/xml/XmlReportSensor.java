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
package me.raatiniemi.sonar.core.xml;

import me.raatiniemi.sonar.core.ReportFinder;
import me.raatiniemi.sonar.core.ReportPatternFinder;
import me.raatiniemi.sonar.core.ReportSensor;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class XmlReportSensor extends ReportSensor {
    private static final Logger LOGGER = Loggers.get(XmlReportSensor.class);

    protected XmlReportSensor(@Nonnull Configuration configuration) {
        super(configuration);
    }

    @Nonnull
    protected final Optional<DocumentBuilder> createDocumentBuilder() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            return Optional.of(factory.newDocumentBuilder());
        } catch (ParserConfigurationException e) {
            LOGGER.error("Unable to create new document builder", e);
            return Optional.empty();
        }
    }

    @Nonnull
    protected final Stream<File> collectAvailableReports(@Nonnull File projectDirectoryPath) {
        ReportPatternFinder reportFinder = ReportFinder.create(projectDirectoryPath);
        return reportFinder.findReportsMatching(readReportPath()).stream();
    }
}
