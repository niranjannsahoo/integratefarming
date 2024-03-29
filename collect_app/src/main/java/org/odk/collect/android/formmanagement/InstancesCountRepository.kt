package org.odk.collect.android.formmanagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.odk.collect.android.database.DatabaseInstancesRepository
import org.odk.collect.android.instances.Instance
import org.odk.collect.android.instances.InstancesRepository
import javax.inject.Singleton

/**
 * Stores reactive state of current instances count with various different statuses. This (as a
 * singleton) can be read or updated by different parts of the app without needing reactive data
 * in the [InstancesRepository].
 */
@Singleton
class InstancesCountRepository {

    private val _unsent = MutableLiveData(0)
    val unsent: LiveData<Int> = _unsent

    private val _finalized = MutableLiveData(0)
    val finalized: LiveData<Int> = _finalized

    private val _sent = MutableLiveData(0)
    val sent: LiveData<Int> = _sent

    fun update() {
        val instancesRepository: InstancesRepository = DatabaseInstancesRepository()
        val finalizedInstances = instancesRepository.getCountByStatus(Instance.STATUS_COMPLETE, Instance.STATUS_SUBMISSION_FAILED)
        val sentInstances = instancesRepository.getCountByStatus(Instance.STATUS_SUBMITTED)
        val unsentInstances = instancesRepository.getCountByStatus(Instance.STATUS_INCOMPLETE, Instance.STATUS_COMPLETE, Instance.STATUS_SUBMISSION_FAILED)

        _finalized.postValue(finalizedInstances)
        _sent.postValue(sentInstances)
        _unsent.postValue(unsentInstances)
    }
}
