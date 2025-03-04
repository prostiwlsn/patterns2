package com.example.h_bankpro.presentation.userCreation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.h_bankpro.R

@Composable
fun RoleToggleSwitch(
    isClientSelected: Boolean,
    onRoleSelected: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        RoleButton(
            text = stringResource(R.string.employee),
            isSelected = !isClientSelected,
            onClick = { onRoleSelected(false) }
        )
        Spacer(modifier = Modifier.width(14.dp))
        RoleButton(
            text = stringResource(R.string.client),
            isSelected = isClientSelected,
            onClick = { onRoleSelected(true) }
        )
    }
}