package com.dbrett.budgie.feature.transactions

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbrett.budgie.core.data.repository.TransactionRepository
import com.dbrett.budgie.core.model.Transaction
import com.dbrett.budgie.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.uuid.Uuid

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TransactionFormState())
    val state = _state.asStateFlow()

    private var currentMode : FormMode = FormMode.CREATE

    val titleState = TextFieldState("")
    val sourceState = TextFieldState("")

    val amountState = TextFieldState("")

    var idState by mutableStateOf<String?>(null)
        private set

    var typeState by mutableStateOf(TransactionType.UNASSIGNED)
        private set

    var timeState by mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
        private set

    fun setId(id : String) {
        idState = id
    }

    fun setMode(mode : FormMode) {
        currentMode = mode
    }

    fun setTransactionType(type : TransactionType) {
        typeState = type
    }

    fun setTime(time : LocalDateTime) {
        timeState = time
    }

    fun setTitleState(title : String) {
        titleState.setTextAndPlaceCursorAtEnd(title)
    }

    fun setSourceState(source : String) {
        sourceState.setTextAndPlaceCursorAtEnd(source)
    }

    fun setAmountState(amount : BigDecimal) {
        amountState.setTextAndPlaceCursorAtEnd(amount.toString())
    }


    fun submit() {
        // values have to be copied here,
        // possibly because launching coro means that the state gets wiped in main thread before the transaction is made.
        var validatedAmount : BigDecimal? = null
        val title = titleState.text.toString()
        val source = sourceState.text.toString()
        val type = typeState

        try {
            validatedAmount = BigDecimal(amountState.text.toString().trim())
        } catch (e : Exception) {
            Log.d("ERROR", "ERROR WHILE SUBMITTING:", e)

            val errorMap = HashMap<FormField, String>()
            errorMap[FormField.AMOUNT] = "Invalid Amount"

            _state.update { it.copy(activeErrors = errorMap) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val constructedTransaction = Transaction(
                id = idState ?: Uuid.random().toString(),
                title = title,
                source = source,
                transactionType = type,
                amount = validatedAmount,
                timestamp = timeState.toInstant(TimeZone.UTC)
            )

            try {
                when (currentMode) {
                    FormMode.CREATE -> {
                        Log.d("DB_TEST", "Attempt Transaction create")
                        transactionRepository.saveTransaction(constructedTransaction)
                        Log.d("DB_TEST", "Transaction created successfully")
                    }
                    FormMode.EDIT -> {
                        Log.d("DB_TEST", "Attempt Transaction update")
                        transactionRepository.updateTransaction(constructedTransaction)
                        Log.d("DB_TEST", "Transaction updated successfully")
                    }
                }
            } catch (dbException: Exception) {
                Log.e("ROOM_ERROR", "Database write failed!", dbException)
            }
        }
    }

    fun reset() {
        titleState.clearText()
        sourceState.clearText()
        amountState.clearText()
        idState = null
        timeState = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        typeState = TransactionType.UNASSIGNED
    }

}

enum class FormMode {
    EDIT, CREATE
}
