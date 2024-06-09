package com.ngwisefood.app.utility;

import java.util.ArrayList;
import java.util.List;

public class MyPagination {

    private int numberPerPage;
    private int selectedPage;
    private int total;
    private int totalPage;
    private List<Integer> pageNumberList = new ArrayList<>();
    private int previousPageNumber;
    private int nextPageNumber;
    private int hiddenPageNumber;
    private int pageOffset = 4;


    public MyPagination() {

    }

    public void init(int numberPerPage, int total) {
        this.numberPerPage = numberPerPage;
        this.total = total;
        if(total % 10 != 0){
            totalPage = total / numberPerPage + 1;
        }else{
            totalPage = total / numberPerPage;
        }
    }

    private void initPageNumber() {
        pageNumberList.clear();

        int maxPageNumber = selectedPage + pageOffset;
        int pageNumberStart = 1;
        if (selectedPage - pageOffset > 1) {
            pageNumberStart = selectedPage - pageOffset;
            pageNumberList.add(-1);
            hiddenPageNumber = pageNumberStart;
        }else{
            hiddenPageNumber = maxPageNumber - 1;
            if(hiddenPageNumber > totalPage){
                hiddenPageNumber = totalPage;
            }else if(hiddenPageNumber < pageNumberStart){
                hiddenPageNumber = pageNumberStart;
            }
        }

        if(previousPageNumber > pageNumberStart){
            previousPageNumber = pageNumberStart;
        }


        for (int i = pageNumberStart; i <= totalPage; i++) {
            if (i < maxPageNumber) {
                pageNumberList.add(i);
            } else if (i >= maxPageNumber) {
                pageNumberList.add(-1);
                break;
            }
        }

    }

    public int getNumberPerPage() {
        return numberPerPage;
    }

    public void setNumberPerPage(int numberPerPage) {
        this.numberPerPage = numberPerPage;
    }

    public int getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(int selectedPage) {

        this.selectedPage = selectedPage;

        if (selectedPage > 1) {
            previousPageNumber = selectedPage - 1;
        } else {
            previousPageNumber = 1;
            this.selectedPage = 1;
        }

        if (selectedPage < totalPage) {
            nextPageNumber = selectedPage + 1;
        } else {
            nextPageNumber = totalPage;
            this.selectedPage = totalPage;
        }

        initPageNumber();
    }

    public List<Integer> getPageNumberList() {
        return pageNumberList;
    }

    public void setPageNumberList(List<Integer> pageNumberList) {
        this.pageNumberList = pageNumberList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPreviousPageNumber() {
        return previousPageNumber;
    }

    public void setPreviousPageNumber(int previousPageNumber) {
        this.previousPageNumber = previousPageNumber;
    }

    public int getNextPageNumber() {
        return nextPageNumber;
    }

    public void setNextPageNumber(int nextPageNumber) {
        this.nextPageNumber = nextPageNumber;
    }

    public int getHiddenPageNumber() {
        return hiddenPageNumber;
    }

    public void setHiddenPageNumber(int hiddenPageNumber) {
        this.hiddenPageNumber = hiddenPageNumber;
    }

    @Override
    public String toString() {
        return "MyPagination{" +
                "numberPerPage=" + numberPerPage +
                ", selectedPage=" + selectedPage +
                ", total=" + total +
                ", totalPage=" + totalPage +
                ", pageNumberList=" + pageNumberList +
                ", previousPageNumber=" + previousPageNumber +
                ", nextPageNumber=" + nextPageNumber +
                ", hiddenPageNumber=" + hiddenPageNumber +
                ", pageOffset=" + pageOffset +
                '}';
    }

    /*

<div th:if="${myPagination.totalPage} > 1" class="box-footer clearfix" >
    <ul class="pagination pagination-sm no-margin pull-right">
      <li><a th:href="@{'/looking-for-job/' + ${myPagination.previousPageNumber}}">&laquo;</a></li>
      <li th:each="pageNumber : ${myPagination.pageNumberList}" th:class="${pageNumber == myPagination.selectedPage} ? 'active' : ''  ">
        <a
            th:if="${pageNumber} > 0"
            th:text="${pageNumber}"
            th:href="@{'/looking-for-job/' + ${pageNumber}}"
        >1</a>
        <a
            th:if="${pageNumber} < 0"
            th:text="..."
            th:href="@{'/looking-for-job/' + ${myPagination.hiddenPageNumber}}"
        >1</a>
      </li>
      <li><a th:href="@{'/looking-for-job/' + ${myPagination.nextPageNumber}}">&raquo;</a></li>
    </ul>
</div>

*/


}
