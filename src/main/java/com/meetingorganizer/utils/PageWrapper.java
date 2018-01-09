package com.meetingorganizer.utils;

import org.springframework.data.domain.Page;

import java.util.LinkedList;
import java.util.List;

public class PageWrapper<T> {

    private static final int MAX_PAGE_NUMBERS_DISPLAYED = 20;

    private int currentNumber;
    private String viewUrl;
    private Page<T> page;
    private List<PageItem> pageItems;

    public PageWrapper(Page<T> page, String viewUrl) {
        this.page = page;
        this.viewUrl = viewUrl;
        this.currentNumber = page.getNumber() + 1;
        this.pageItems = new LinkedList<>();

        addItemsToPageWrapper(page);
    }

    void addItemsToPageWrapper(Page<T> page) {
        int start, size;

        if (page.getTotalPages() <= MAX_PAGE_NUMBERS_DISPLAYED) {
            start = 1;
            size = page.getTotalPages();
        } else {

            if (displayPageNumbersFromStart()) {
                start = 1;
                size = MAX_PAGE_NUMBERS_DISPLAYED;
            } else if (displayPageNumbersInTheMiddle()) {
                start = page.getTotalPages() - MAX_PAGE_NUMBERS_DISPLAYED + 1;
                size = MAX_PAGE_NUMBERS_DISPLAYED;
            } else {
                start = currentNumber - (MAX_PAGE_NUMBERS_DISPLAYED / 2);
                size = MAX_PAGE_NUMBERS_DISPLAYED;
            }
        }

        for (int i = 0; i < size; i++) {
            pageItems.add(new PageItem(start + i, (start + i) == currentNumber));
        }
    }

    boolean displayPageNumbersFromStart() {
        return currentNumber <= (MAX_PAGE_NUMBERS_DISPLAYED - (MAX_PAGE_NUMBERS_DISPLAYED / 2));
    }

    boolean displayPageNumbersInTheMiddle() {
        return currentNumber >= (page.getTotalPages() - (MAX_PAGE_NUMBERS_DISPLAYED / 2));
    }

    public boolean isFirstPage() {
        return page.isFirst();
    }

    public boolean isLastPage() {
        return page.isLast();
    }

    public int getCurrentNumber() {
        return currentNumber;
    }

    public int getTotalPages() {
        return page.getTotalPages();
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public int getSize() {
        return page.getSize();
    }

    public Page<T> getPage() {
        return page;
    }

    public List<PageItem> getPageItems() {
        return pageItems;
    }

    public class PageItem {
        private int number;
        private boolean current;

        public PageItem(int number, boolean current) {
            this.number = number;
            this.current = current;
        }

        public int getNumber() {
            return this.number;
        }

        public boolean isCurrent() {
            return this.current;
        }
    }
}
