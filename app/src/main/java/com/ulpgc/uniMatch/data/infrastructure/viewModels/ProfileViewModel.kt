package com.ulpgc.uniMatch.data.infrastructure.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.Habits
import com.ulpgc.uniMatch.data.domain.enums.Horoscope
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.enums.Religion
import com.ulpgc.uniMatch.data.domain.enums.SexualOrientation
import com.ulpgc.uniMatch.data.domain.models.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ProfileViewModel(
    private val profileService: ProfileService,
    private val errorViewModel: ErrorViewModel,
    private val userViewModel: UserViewModel
) : ViewModel() {

    private val _profileData = MutableStateFlow<Profile?>(null)
    val profileData: StateFlow<Profile?> get() = _profileData

    private val _editedProfile = MutableStateFlow<Profile?>(null)
    val editedProfile: StateFlow<Profile?> get() = _editedProfile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun loadProfile() {
        performLoadingAction {
            val result = profileService.getProfile(userViewModel.userId!!)
            result.onSuccess { profile ->
                _profileData.value = profile
                _editedProfile.value = profile.copy()
            }
        }
    }

    fun hasUnsavedChanges(): Boolean = _profileData.value != _editedProfile.value

    private fun <T> updateField(
        fieldUpdater: (Profile, T) -> Profile,
        newValue: T,
        serviceCall: suspend (T) -> Result<T>,
        fieldSelector: (Profile) -> T?
    ) {
        val currentProfile = _profileData.value ?: return
        val editedProfile = _editedProfile.value ?: return
        if (fieldSelector(currentProfile) == newValue && fieldSelector(editedProfile) == newValue) return

        performLoadingAction {
            val result = serviceCall(newValue)
            result.onSuccess { updatedValue ->
                _profileData.value = fieldUpdater(currentProfile, updatedValue)
                _editedProfile.value = fieldUpdater(editedProfile, updatedValue)
            }.onFailure { error ->
                Log.e("ProfileViewModel", "Error updating field", error)
                _editedProfile.value = _profileData.value
                errorViewModel.showError(error.message ?: "Error updating field")
            }
        }
    }


    private fun performLoadingAction(action: suspend () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                action()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile() {
        performLoadingAction {
            try {
                updateAboutMe()
                updateFact()
                updateHeight()
                updateWeight()
                updateJob()
                updateHoroscope()
                updateEducation()
                updatePersonalityType()
                updatePets()
                updateDrinks()
                updateSmokes()
                updateDoesSports()
                updateValuesAndBeliefs()
                updateGender()
                updateSexualOrientation()

            } catch (error: Exception) {
                errorViewModel.showError(error.message ?: "Error updating profile")
            }
        }
    }


    fun updateInterests(interests: List<String>) = updateField(
        fieldUpdater = { profile, value -> profile.copy(interests = value) },
        newValue = interests,
        serviceCall = profileService::updateInterests,
        fieldSelector = Profile::interests
    )

    private fun updateAboutMe() = _editedProfile.value?.let { updateField(
        fieldUpdater = { profile, value -> profile.copy(aboutMe = value) },
        newValue = it.aboutMe,
        serviceCall = profileService::updateAboutMe,
        fieldSelector = Profile::aboutMe
        )
    }

    fun updateAboutMe(aboutMe: String) {
        _editedProfile.value = _editedProfile.value?.copy(aboutMe = aboutMe)
    }

    private fun updateJob() = updateField(
        fieldUpdater = { profile, value -> profile.copy(job = value) },
        newValue = _editedProfile.value?.job,
        serviceCall = profileService::updateJob,
        fieldSelector = Profile::job
    )

    fun updateJob(job: String?) {
        _editedProfile.value = _editedProfile.value?.copy(job = job)
    }

    private fun updateHoroscope() {
        val horoscope = _editedProfile.value?.horoscope ?: return
        updateField(
            fieldUpdater = { profile, newHoroscope -> profile.copy(horoscope = newHoroscope) },
            newValue = horoscope,
            serviceCall = profileService::updateHoroscope,
            fieldSelector = Profile::horoscope
        )
    }

    fun updateHoroscope(horoscope: Horoscope?) {
        _editedProfile.value = _editedProfile.value?.copy(horoscope = horoscope)
    }

    private fun updateEducation() = updateField(
        fieldUpdater = { profile, value -> profile.copy(education = value) },
        newValue = _editedProfile.value?.education,
        serviceCall = profileService::updateEducation,
        fieldSelector = Profile::education
    )

    fun updateEducation(education: String?) {
        _editedProfile.value = _editedProfile.value?.copy(education = education)
    }

    private fun updatePersonalityType() = updateField(
        fieldUpdater = { profile, value -> profile.copy(personalityType = value) },
        newValue = _editedProfile.value?.personalityType,
        serviceCall = profileService::updatePersonalityType,
        fieldSelector = Profile::personalityType
    )

    fun updatePersonalityType(personalityType: String?) {
        _editedProfile.value = _editedProfile.value?.copy(personalityType = personalityType)
    }

    private fun updatePets() = updateField(
        fieldUpdater = { profile, value -> profile.copy(pets = value) },
        newValue = _editedProfile.value?.pets,
        serviceCall = profileService::updatePets,
        fieldSelector = Profile::pets
    )

    fun updatePets(pets: String?) {
        _editedProfile.value = _editedProfile.value?.copy(pets = pets)
    }

    private fun updateDrinks() = updateField(
        fieldUpdater = { profile, value -> profile.copy(drinks = value) },
        newValue = _editedProfile.value?.drinks,
        serviceCall = profileService::updateDrinks,
        fieldSelector = Profile::drinks
    )

    fun updateDrinks(drinks: Habits?) {
        _editedProfile.value = _editedProfile.value?.copy(drinks = drinks)
    }

    private fun updateSmokes() = updateField(
        fieldUpdater = { profile, value -> profile.copy(smokes = value) },
        newValue = _editedProfile.value?.smokes,
        serviceCall = profileService::updateSmokes,
        fieldSelector = Profile::smokes
    )

    fun updateSmokes(smokes: Habits?) {
        _editedProfile.value = _editedProfile.value?.copy(smokes = smokes)
    }

    private fun updateDoesSports() = updateField(
        fieldUpdater = { profile, value -> profile.copy(doesSports = value) },
        newValue = _editedProfile.value?.doesSports,
        serviceCall = profileService::updateDoesSports,
        fieldSelector = Profile::doesSports
    )

    fun updateDoesSports(doesSports: Habits?) {
        _editedProfile.value = _editedProfile.value?.copy(doesSports = doesSports)
    }

    private fun updateValuesAndBeliefs() = updateField(
        fieldUpdater = { profile, value -> profile.copy(valuesAndBeliefs = value) },
        newValue = _editedProfile.value?.valuesAndBeliefs,
        serviceCall = profileService::updateValuesAndBeliefs,
        fieldSelector = Profile::valuesAndBeliefs
    )

    fun updateValuesAndBeliefs(valuesAndBeliefs: Religion?) {
        _editedProfile.value = _editedProfile.value?.copy(valuesAndBeliefs = valuesAndBeliefs)
    }

    private fun updateWeight() = updateField(
        fieldUpdater = { profile, value -> profile.copy(weight = value) },
        newValue = _editedProfile.value?.weight,
        serviceCall = profileService::updateWeight,
        fieldSelector = Profile::weight
    )

    fun updateWeight(weight: Int?) {
        _editedProfile.value = _editedProfile.value?.copy(weight = weight)
    }

    private fun updateHeight() = updateField(
        fieldUpdater = { profile, value -> profile.copy(height = value) },
        newValue = _editedProfile.value?.height,
        serviceCall = profileService::updateHeight,
        fieldSelector = Profile::height
    )

    fun updateHeight(height: Int?) {
        _editedProfile.value = _editedProfile.value?.copy(height = height)
    }

    private fun updateFact() = updateField(
        fieldUpdater = { profile, value -> profile.copy(fact = value) },
        newValue = _editedProfile.value?.fact,
        serviceCall = profileService::updateFact,
        fieldSelector = Profile::fact
    )

    fun updateFact(fact: String) {
        _editedProfile.value = _editedProfile.value?.copy(fact = fact)
    }

    private fun updateSexualOrientation(){
        val sexualOrientation = _editedProfile.value?.sexualOrientation ?: return
        updateField(
            fieldUpdater = { profile, newSexualOrientation -> profile.copy(sexualOrientation = newSexualOrientation) },
            newValue = sexualOrientation,
            serviceCall = profileService::updateSexualOrientation,
            fieldSelector = Profile::sexualOrientation
        )
    }

    fun updateSexualOrientation(orientation: SexualOrientation) {
        _editedProfile.value = _editedProfile.value?.copy(sexualOrientation = orientation)
    }

    private fun updateGender(){
        val gender = _editedProfile.value?.gender ?: return
        updateField(
            fieldUpdater = { profile, value -> profile.copy(gender = value) },
            newValue = gender,
            serviceCall = profileService::updateGender,
            fieldSelector = Profile::gender
        )
    }

    fun updateGender(gender: Gender) {
        _editedProfile.value = _editedProfile.value?.copy(gender = gender)
    }

    fun updateRelationshipType(relationshipType: RelationshipType) {
        updateField(
            fieldUpdater = { profile, value -> profile.copy(relationshipType = value) },
            newValue = relationshipType,
            serviceCall = profileService::updateRelationshipType,
            fieldSelector = Profile::relationshipType
        )
    }

    fun updateAgeRange(min: Int, max: Int) {
        val currentProfile = _profileData.value ?: return
        val editedProfile = _editedProfile.value ?: return
        if (currentProfile.ageRange == Profile.AgeRange(min, max)) return

        performLoadingAction {
            val result = profileService.updateAgeRange(min, max)
            result.onSuccess {
                _profileData.value = currentProfile.copy(ageRange = Profile.AgeRange(min, max))
                _editedProfile.value = editedProfile.copy(ageRange = Profile.AgeRange(min, max))
            }.onFailure { error ->
                errorViewModel.showError(error.message ?: "Error updating age range")
            }
        }
    }

    fun updateGenderPriority (gender: Gender?) {
        updateField(
            fieldUpdater = { profile, value -> profile.copy(genderPriority = value) },
            newValue = gender,
            serviceCall = profileService::updateGenderPriority,
            fieldSelector = Profile::genderPriority
        )
    }

    fun updateMaxDistance(maxDistance: Int) = updateField(
        fieldUpdater = { profile, value -> profile.copy(maxDistance = value) },
        newValue = maxDistance,
        serviceCall = profileService::updateMaxDistance,
        fieldSelector = Profile::maxDistance
    )

    fun updateLocation(location: Profile.Location?) = updateField(
        fieldUpdater = { profile, value -> profile.copy(location = value) },
        newValue = location,
        serviceCall = profileService::updateLocation,
        fieldSelector = Profile::location
    )

    fun updateWall(wall: List<String>) = updateField(
        fieldUpdater = { profile, value -> profile.copy(wall = value) },
        newValue = wall,
        serviceCall = profileService::updateWall,
        fieldSelector = Profile::wall
    )

    fun addImage(imageUrl: Uri) {
        performLoadingAction {
            val result = profileService.addImage(imageUrl)
            result.onSuccess { imageUrlApi ->
                _profileData.value = _profileData.value?.copy(wall = _profileData.value?.wall.orEmpty() + imageUrlApi)
                _editedProfile.value = _editedProfile.value?.copy(wall = _editedProfile.value?.wall.orEmpty() + imageUrlApi)
            }.onFailure { error ->
                errorViewModel.showError(error.message ?: "Error adding image")
            }
        }
    }

    fun deleteImage(imageUrl: String) {
        performLoadingAction {
            val result = profileService.removeImage(imageUrl)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(wall = (_profileData.value?.wall.orEmpty() - imageUrl))
                _editedProfile.value = _editedProfile.value?.copy(wall = (_editedProfile.value?.wall.orEmpty() - imageUrl))
            }.onFailure { error ->
                errorViewModel.showError(error.message ?: "Error deleting image")
            }
        }
    }
}

