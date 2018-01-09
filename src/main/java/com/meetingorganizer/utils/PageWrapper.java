package com.meetingorganizer.utils;

import org.springframework.data.domain.Page;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class PageWrapper<T> {

    private static final int MAX_PAGE_NUMBERS_DISPLAYED = 20;
    public static final int[] PAGE_SIZES = new int[] {5, 10, 15, 20};
    public static final int DEFAULT_PAGE_SIZE = 10;

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

    public int[] getPageSizes() {
        return PAGE_SIZES;
    }

    public List<PageItem> getPageItems() {
        return pageItems;
    }

    public boolean isPageSizeValueEqualToRequest(Integer selectPageSize, Integer requestPageSize) {
        if(requestPageSize == null) {
            return selectPageSize == DEFAULT_PAGE_SIZE ? true : false;
        } else {
            return selectPageSize == requestPageSize;
        }
    }

    public static int adjustPageNumber(int page) {
        return page > 0 ? page : 1;
    }

    public static int adjustPageSize(int pageSize) {
        boolean pageSizeAvailable = IntStream.of(PAGE_SIZES).anyMatch(size -> size == pageSize);
        return pageSizeAvailable ? pageSize : DEFAULT_PAGE_SIZE;
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
