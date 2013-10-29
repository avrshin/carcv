/*
 * Copyright 2012 CarCV Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.carcv.web.reports;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.carcv.core.model.Address;
import org.carcv.core.model.CarData;
import org.carcv.core.model.file.FileEntry;

/**
 *
 */
public class BasicReportGenerator {

    JasperPrint filledReportPrint;

    public BasicReportGenerator(FileEntry e, String templateFilename, String reportBuilderLocation,
        String reportName) throws JRException {

        Map<String, Object> values = new HashMap<String, Object>();
        Map<String, Object> parameters = new HashMap<String, Object>();

        CarData data = e.getCarData();

        DateFormat dateFormat = new SimpleDateFormat("dd. MM. yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // report
        parameters.put("reportid", Long.toString(System.currentTimeMillis()));
        parameters.put("reportname", reportName);
        parameters.put("reportlocation", reportBuilderLocation);
        parameters.put("reportdate", dateFormat.format(new Date(System.currentTimeMillis())));

        // data
        Address add = data.getAddress();
        String dataLocation = add.print();

        // parameters.put("id", Long.toString(data.getId()));
        parameters.put("previewURL", e.getCarImages().get(0).getPath().toString());
        parameters.put("date", dateFormat.format(data.getTimestamp()));
        parameters.put("location", dataLocation);
        parameters.put("LPNumber", data.getNumberPlate().getText());
        parameters.put("videoURL", "/servlet/GenerateVideo?entry_id=" + e.getId().toString());
        parameters.put("time", timeFormat.format(data.getTimestamp()));
        parameters.put("speed", Double.toString(data.getSpeed().getSpeed()) + " "
            + data.getSpeed().getUnit().toString());

        // parameters.put

        Collection<Map<String, ?>> mapList = new ArrayList<Map<String, ?>>();
        mapList.add(values);

        JRMapCollectionDataSource mapDataSource = new JRMapCollectionDataSource(mapList);

        // compile template - already precompiled
        // JasperCompileManager.compileReportToFile(templateFilename + ".jrxml",
        // templateFilename + ".jasper");

        // fill with data
        InputStream templateInputStream = getClass().getResourceAsStream(templateFilename);

        filledReportPrint = JasperFillManager.fillReport(templateInputStream, parameters, mapDataSource);

    }

    public void exportFile(String filename) throws JRException {
        JRExporter exporter = new JRPdfExporter();

        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filename);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, filledReportPrint);
        exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");

        exporter.exportReport();

        return;
    }

    public void exportStream(String filename, OutputStream out) throws JRException {

        JRExporter exporter = new JRPdfExporter();

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, filledReportPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filename);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");

        exporter.exportReport();
    }

}