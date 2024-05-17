package com.example.learnopensearch;

import com.example.learnopensearch.model.Widget;
import com.example.learnopensearch.service.DuplicateSerialNumberException;
import com.example.learnopensearch.service.WidgetNotFoundException;
import com.example.learnopensearch.service.WidgetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.opensearch.testcontainers.OpensearchContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LearnopensearchIntegrationTest {

    @Autowired
    private WidgetService widgetService;
    @Autowired
    private ElasticsearchOperations operations;

    private static final DockerImageName OPENSEARCH_IMAGE = DockerImageName.parse("opensearchproject/opensearch:2.11.0");


    @Container
    public static OpensearchContainer<?> opensearch = new OpensearchContainer<>(OPENSEARCH_IMAGE);


    @BeforeAll
    static void beforeAll() throws InterruptedException {


        opensearch.start();
        Assertions.assertTrue(opensearch.isRunning());

        System.out.println("Sleeping for 30 seconds to allow OpenSearch to start up...");

        opensearch.getExposedPorts().forEach(port -> {
            System.out.println("OpenSearch is running on port " + port);
        });

        System.out.println("OpenSearch is running on port " + opensearch.getMappedPort(9200));
    }

    @BeforeEach
    void testIsContainerRunning() {
        recreateIndex();
    }

    @Test
    void testGetWidgetsBySerialNumber() throws DuplicateSerialNumberException {
        widgetService.create(createWidget("Ultra Pump", "ACME", 2018, "1234567890"));
        Optional<Widget> result = widgetService.findBySerialNumber("1234567890");
        assertTrue(result.isPresent());
        Widget getWidget = result.get();
        assertNotNull(getWidget);
        assertEquals("Ultra Pump", getWidget.getName());
        assertEquals("ACME", getWidget.getBrandName());
        assertEquals(2018, getWidget.getManufactureYear());
        assertEquals("1234567890", getWidget.getSerialNumber());
    }

    @Test
    void testGetAllWidgets() throws DuplicateSerialNumberException {

        widgetService.create(createWidget("Ultra Pump", "ACME", 2018, "1234567890"));
        widgetService.create(createWidget("Ultra Pump", "ACME", 2018, "0987654321"));
        List<Widget> widgets = widgetService.getAll();

        assertNotNull(widgets);
        assertEquals(2, widgets.size());
    }

    @Test
    void testFindByBrand() throws DuplicateSerialNumberException {
        widgetService.create(createWidget("Ultra Pump", "ACME", 2018, "1234567890"));
        widgetService.create(createWidget("Ultra Pump", "ACME", 2018, "0987654321"));

        List<Widget> widgets = widgetService.findByBrandName("ACME");

        assertNotNull(widgets);
        assertEquals(2, widgets.size());
    }

    @Test
    void testFindByNameAndBrand() throws DuplicateSerialNumberException {
        widgetService.create(createWidget("ACME 5000", "ACME", 2018, "1234567890"));
        widgetService.create(createWidget("ACME 2000", "ACME", 2014, "0987654321"));
        widgetService.create(createWidget("Dolor Supreme", "Ipsum", 2013, "11111111111"));
        widgetService.create(createWidget("Ultra Pump", "Kirkland", 2012, "22222222222"));

        List<Widget> widgets = widgetService.findByNameAndBrandName("5000", "acme");

        assertNotNull(widgets);
        assertEquals(1, widgets.size());
    }

    @Test
    void testCreateWidget() throws DuplicateSerialNumberException {
        Widget createdWidget = widgetService.create(createWidget("ACME 5000", "ACME", 2018, "1234567890"));
        assertNotNull(createdWidget);
        assertNotNull(createdWidget.getId());
        assertEquals("ACME 5000", createdWidget.getName());
        assertEquals("ACME", createdWidget.getBrandName());
        assertEquals(2018, createdWidget.getManufactureYear());
        assertEquals("1234567890", createdWidget.getSerialNumber());
    }

    @Test
    void testCreateWidgetWithDuplicateSerialNumberThrowsException() throws DuplicateSerialNumberException {
        Widget createdWidget = widgetService.create(createWidget("ACME 5000", "ACME", 2018, "1234567890"));
        assertNotNull(createdWidget);
        assertThrows(DuplicateSerialNumberException.class, () -> {
            widgetService.create(createWidget("TEST", "TEST", 2222, "1234567890"));
        });
    }

    @Test
    void testDeleteWidgetById() throws DuplicateSerialNumberException {
        Widget createdWidget = widgetService.create(createWidget("ACME 5000", "ACME", 2018, "1234567890"));
        assertNotNull(createdWidget);
        assertNotNull(createdWidget.getId());

        widgetService.deleteById(createdWidget.getId());
        List<Widget> widgets = widgetService.findByBrandName("ACME");

        assertTrue(widgets.isEmpty());
    }

    @Test
    void testUpdateWidget() throws DuplicateSerialNumberException, WidgetNotFoundException {
        Widget widgetToUpdate = widgetService.create(createWidget("ACME 5000", "ACME", 2018, "1234567890"));

        assertNotNull(widgetToUpdate);
        assertNotNull(widgetToUpdate.getId());

        widgetToUpdate.setManufactureYear(2019);
        Widget updatedWidget = widgetService.update(widgetToUpdate.getId(), widgetToUpdate);

        assertNotNull(updatedWidget);
        assertNotNull(updatedWidget.getId());
        assertEquals("ACME 5000", updatedWidget.getName());
        assertEquals("ACME", updatedWidget.getBrandName());
        assertEquals(2019, updatedWidget.getManufactureYear());
        assertEquals("1234567890", updatedWidget.getSerialNumber());
    }

    @Test
    void testUpdateWidgetThrowsWidgetNotFoundException() {
        Widget updatedWidget = createWidget("ACME 5000", "ACME", 2018, "1234567890");

        assertThrows(WidgetNotFoundException.class, () -> {
            widgetService.update("1A2B3C", updatedWidget);
        });
    }

    private Widget createWidget(String name, String brandName, int manufactureYear, String serialNumber) {
        Widget widget = new Widget();
        widget.setName(name);
        widget.setBrandName(brandName);
        widget.setManufactureYear(manufactureYear);
        widget.setSerialNumber(serialNumber);
        return widget;
    }

    private void recreateIndex() {
        IndexOperations indexOperations = operations.indexOps(Widget.class);
        if (indexOperations.exists()) {
            indexOperations.delete();
            indexOperations.create();
            indexOperations.refresh();
        }
    }
}