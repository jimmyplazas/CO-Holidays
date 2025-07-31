package dev.alejo.colombian_holidays.widget

import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import dev.alejo.colombian_holidays.MainActivity
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.core.Response
import dev.alejo.colombian_holidays.domain.repository.Repository
import dev.alejo.colombian_holidays.ui.util.DateUtils
import dev.alejo.colombian_holidays.ui.util.ImagesProvider
import org.koin.core.context.GlobalContext
import java.time.LocalDate
import java.util.Locale

class AppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val repository = GlobalContext.get().get<Repository>()
        val response = repository.getHolidaysByYear(LocalDate.now().year.toString())
        if (response is Response.Success) {
            val isSpanish = Locale.getDefault().language == "es"
            val today = LocalDate.now()
            val image = ImagesProvider.getImages().random()
            val todayHoliday = response.data.find { it.date == today }
            val nextHoliday = response.data.firstOrNull { it.date.isAfter(today) }

            val mainText = when {
                todayHoliday == null -> context.getString(R.string.today_is_not_holiday)
                isSpanish -> todayHoliday.localName
                else -> todayHoliday.name
            }

            provideContent {
                val nextHolidayMessage = nextHoliday?.let {
                    DateUtils.getNextHolidayMessage(context, nextHoliday)
                } ?: ""

                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .clickable(actionStartActivity<MainActivity>())
                        .cornerRadius(16.dp)
                        .background(GlanceTheme.colors.background)
                ) {
                    Image(
                        provider = ImageProvider(image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = GlanceModifier
                    )

                    Column(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .background(R.color.widget_background)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (todayHoliday != null) {
                            Text(
                                text = context.getString(R.string.today_is_holiday),
                                modifier = GlanceModifier.fillMaxWidth(),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    color = GlanceTheme.colors.onBackground
                                )
                            )
                            Spacer(modifier = GlanceModifier.height(8.dp))
                        }
                        Text(
                            text = mainText,
                            modifier = GlanceModifier.fillMaxWidth(),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                                textAlign = TextAlign.Center,
                                color = GlanceTheme.colors.onBackground
                            )
                        )
                        Spacer(modifier = GlanceModifier.height(8.dp))
                        Text(
                            text = nextHolidayMessage,
                            modifier = GlanceModifier.fillMaxWidth(),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                color = GlanceTheme.colors.onBackground
                            )
                        )
                    }

                }
            }

        } else {
            provideContent {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .clickable(actionStartActivity<MainActivity>())
                        .cornerRadius(16.dp)
                        .background(R.color.black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = context.getString(R.string.unexpected_error),
                        modifier = GlanceModifier.fillMaxWidth(),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = GlanceTheme.colors.error
                        )
                    )
                }
            }
        }
    }
}