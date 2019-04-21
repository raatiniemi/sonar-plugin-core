/*
 * Copyright (c) 2019 Tobias Raatiniemi
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

import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.annotation.Nonnull;
import java.util.Optional;

public abstract class ReportSensor implements Sensor {
    private static final Logger LOGGER = Loggers.get(ReportSensor.class);

    private final Configuration configuration;

    protected ReportSensor(@Nonnull Configuration configuration) {
        this.configuration = configuration;
    }

    @Nonnull
    protected String readReportPath() {
        String reportPathKey = getReportPathKey();
        Optional<String> value = configuration.get(reportPathKey);
        if (value.isPresent()) {
            LOGGER.debug("Found report path for configuration key {}", reportPathKey);
            return value.get();
        }

        LOGGER.debug("Found no report path for configuration key {}, using default path", reportPathKey);
        return getDefaultReportPath();
    }

    @Nonnull
    protected abstract String getReportPathKey();

    @Nonnull
    protected abstract String getDefaultReportPath();
}
