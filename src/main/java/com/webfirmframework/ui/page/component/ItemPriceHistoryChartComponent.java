package com.webfirmframework.ui.page.component;

import com.webfirmframework.ui.page.common.GlobalSTC;
import com.webfirmframework.ui.page.model.DocumentModel;
import com.webfirmframework.wffweb.server.page.BrowserPageContext;
import com.webfirmframework.wffweb.tag.html.H1;
import com.webfirmframework.wffweb.tag.html.Hr;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.global.Id;
import com.webfirmframework.wffweb.tag.html.attribute.global.Style;
import com.webfirmframework.wffweb.tag.html.programming.Script;
import com.webfirmframework.wffweb.tag.html.stylesandsemantics.Div;
import com.webfirmframework.wffweb.tag.htmlwff.TagContent;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemPriceHistoryChartComponent extends Div {

    private static final ScheduledExecutorService SCHEDULED_THREAD_POOL = Executors.newScheduledThreadPool(1);

    private final DocumentModel documentModel;

    private final long itemId;

    private volatile ScheduledFuture<?> scheduledFuture;

    public ItemPriceHistoryChartComponent(DocumentModel documentModel, long itemId) {
        super(null);
        this.documentModel = documentModel;
        this.itemId = itemId;
        GlobalSTC.LOGGER_STC.setContent(
                ZonedDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) +
                        ":~$ created new ItemPriceHistoryChartComponent");
        develop();
    }

    private void develop() {

        new H1(this).give(TagContent::text, String.format("Item %s Price History Live data", itemId));
        new Hr(this);

        new Div(this, new Id("chart_div"), new Style("width: 100%; height: 500px;"));

        new Script(this, new Type(Type.TEXT_JAVASCRIPT))
                .give(TagContent::text, """
                        google.charts.load('current', {'packages':['corechart']});
                        google.charts.setOnLoadCallback(drawChart);
                                                
                        var options = {
                            legend:'none'
                        };

                        var chart;
                        var chartData=[];
                        function drawChart() {
                            chartData = [];
                            var data = google.visualization.arrayToDataTable(chartData, true);
                            chart = new google.visualization.CandlestickChart(document.getElementById('chart_div'));
                            chart.draw(data, options);
                        };
                                                
                        this.updateChart = function (perDayData) {
                            
                            if (chartData.length > 9) {
                                chartData.shift();
                            }
                            chartData.push(perDayData);
                            if (google && google.visualization) {
                                var data = google.visualization.arrayToDataTable(chartData, true);
                                chart.draw(data, options);
                            }
                        };
                        """.stripIndent());

        LocalDate localDate = LocalDate.now().minusYears(2);
        AtomicInteger atomicInteger = new AtomicInteger();
        Random random = new Random();
        scheduledFuture = SCHEDULED_THREAD_POOL.scheduleAtFixedRate(() -> {

            try {
                if (documentModel.browserPage().contains(ItemPriceHistoryChartComponent.this)
                        && BrowserPageContext.INSTANCE.existsAndValid(documentModel.browserPage())) {

                    int a1 = random.nextInt(1, 100);
                    int d1 = random.nextInt(1, 100);

                    int a = Math.min(a1, d1);
                    int d = Math.max(a1, d1);
                    d = a == d ? d + 10 : d;

                    int b = random.nextInt(a, d);
                    int c = random.nextInt(a, d);

                    String js = "if (updateChart) {updateChart(['" + localDate.plusDays(atomicInteger.incrementAndGet()) + "', " +
                            a + ", " +
                            b + ", " +
                            c + ", " +
                            d + "])};";
                    documentModel.browserPage().getTagRepository().executeJs(js);
                } else {
                    scheduledFuture.cancel(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, 2, 1, TimeUnit.SECONDS);


    }

    public long getItemId() {
        return itemId;
    }
}
