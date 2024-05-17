package com.example.learnopensearch.service;

import com.example.learnopensearch.model.Widget;
import com.example.learnopensearch.model.WidgetRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WidgetServiceImpl implements WidgetService {

    private final WidgetRepository widgetRepository;

    public WidgetServiceImpl(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    @Override
    public List<Widget> findByName(String name) {
        return widgetRepository.findByName(name);
    }

    @Override
    public List<Widget> findByBrandName(String brandName) {
        return widgetRepository.findByBrandName(brandName);
    }

    @Override
    public Optional<Widget> findBySerialNumber(String serialNumber) {
        return widgetRepository.findBySerialNumber(serialNumber);
    }

    @Override
    public List<Widget> getAll() {
        List<Widget> widgets = new ArrayList<>();

        widgetRepository.findAll().forEach(widgets::add);

        return widgets;
    }

    @Override
    public Optional<Widget> getById(String id) {
        return widgetRepository.findById(id);
    }

    @Override
    public Widget create(Widget widget) throws DuplicateSerialNumberException {
        Optional<Widget> existingWidget = widgetRepository.findBySerialNumber(widget.getSerialNumber());
        if (existingWidget.isPresent()) {
            throw new DuplicateSerialNumberException("Widget with serial number " + widget.getSerialNumber() + " already exists");
        }
        return widgetRepository.save(widget);
    }

    @Override
    public void deleteById(String id) {
        widgetRepository.deleteById(id);
    }

    @Override
    public Widget update(String id, Widget widget) throws WidgetNotFoundException {
        Optional<Widget> existingWidget = widgetRepository.findById(id);
        if (existingWidget.isEmpty()) {
            throw new WidgetNotFoundException("Widget with id " + id + " not found");
        }
        widget.setId(id);
        return widgetRepository.save(widget);
    }

    @Override
    public List<Widget> findByNameAndBrandName(String name, String brandName) {
        return widgetRepository.findByNameAndBrandName(name, brandName);
    }

    @Override
    public List<Widget> search(String query) {
        return widgetRepository.search(query);
    }
}
