package com.example.learnopensearch.controller;

import com.example.learnopensearch.model.Widget;
import com.example.learnopensearch.service.DuplicateSerialNumberException;
import com.example.learnopensearch.service.WidgetNotFoundException;
import com.example.learnopensearch.service.WidgetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/widgets")
public class WidgetController {

    private final WidgetService widgetService;

    public WidgetController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @PostMapping
    public Widget createWidget(@RequestBody Widget widget) throws DuplicateSerialNumberException {
        return widgetService.create(widget);
    }

    @GetMapping
    public List<Widget> getAllWidgets() {
        return widgetService.getAll();
    }

    @GetMapping("/{id}")
    public Widget getWidget(@PathVariable String id) {
        return widgetService.getById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Widget updateWidget(@RequestBody Widget widget, @PathVariable String id) throws WidgetNotFoundException {
        return widgetService.update(id, widget);
    }

    @DeleteMapping("/{id}")
    public void deleteWidget(@PathVariable String id) {
        widgetService.deleteById(id);
    }

    @GetMapping(value = "/query")
    public List<Widget> getWidgetsByNameAndBrandName(@RequestParam(value = "name") String name, @RequestParam(value = "brandName") String brandName) {
        return widgetService.findByNameAndBrandName(name, brandName);
    }

    @GetMapping(value = "/search")
    public List<Widget> searchWidgets(@RequestParam(value = "query") String query) {
        return widgetService.search(query);
    }
}

