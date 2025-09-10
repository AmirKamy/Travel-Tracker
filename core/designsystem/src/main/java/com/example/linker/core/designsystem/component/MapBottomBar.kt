package com.example.linker.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.osmdroid.util.GeoPoint

@Composable
fun MapBottomBar(
    modifier: Modifier = Modifier,
    tracking: Boolean,
    canExport: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onExport: () -> Unit,
    polyline: List<GeoPoint>,
    startFakeRoutes: (() -> Unit)?
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(), // احترام به ناوبری گوشی
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shadowElevation = 12.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .imePadding(), // وقتی کیبورد باز شد بالا بره
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // هندل کوچک بالا
            Box(
                Modifier
                    .width(48.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(0.5.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            )
            Spacer(Modifier.height(12.dp))

            // ردیف اکشن‌ها: شروع/پایان + دانلود خروجی
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (!tracking) {
                    FilledTonalButton(
                        onClick = onStart,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) { Text("شروع") }
                } else {
                    FilledTonalButton(
                        onClick = onStop,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) { Text("پایان") }

                    // با این دکمه تست کنید نقاط فیک را
                    if (startFakeRoutes != null)
                        FilledTonalButton(
                            onClick = startFakeRoutes,
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 14.dp)
                        ) { Text("نقاط فیک در دیباگ مود") }
                }
                if (!tracking && polyline.isNotEmpty())
                    Button(
                        onClick = onExport,
                        enabled = canExport,
                        modifier = Modifier.weight(1.4f),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) { Text("دانلود خروجی") }


            }

            Spacer(Modifier.height(4.dp))
        }
    }
}
