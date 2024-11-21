package com.ulpgc.uniMatch.data.infrastructure.viewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enums.Gender
import com.ulpgc.uniMatch.data.domain.enums.RelationshipType
import com.ulpgc.uniMatch.data.domain.models.Profile
import com.ulpgc.uniMatch.ui.screens.utils.enumToString
import com.ulpgc.uniMatch.ui.screens.utils.stringToEnum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



open class ProfileViewModel(
    private val profileService: ProfileService,
    private val errorViewModel: ErrorViewModel,
    private val userViewModel: UserViewModel
) : ViewModel() {

    private val _profileData = MutableStateFlow<Profile?>(null)
    val profileData: StateFlow<Profile?> get() = _profileData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private var _editedProfile = MutableStateFlow<Profile?>(null)
    val editedProfile: StateFlow<Profile?> get() = _editedProfile


    fun getProfileData(): Profile? {
        return _profileData.value
    }


    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.getProfile(userViewModel.userId!!)
            result.onSuccess { profileData ->
                _profileData.value = profileData
                _editedProfile.value = profileData.copy()
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error loading profile"
                )
                _isLoading.value = false
            }
        }
    }

    fun changeAboutMe(aboutMe: String) {
        _editedProfile.value?.aboutMe = aboutMe
    }

    fun changeFact(fact: String) {
        _editedProfile.value?.fact = fact
    }

    fun changeHeight(height: Int?) {
        _editedProfile.value?.height = height
    }

    fun changeWeight(weight: Int?) {
        _editedProfile.value?.weight = weight
    }

    fun changeWallOrder(wall: List<String>) {
        Log.i("ProfileViewModel", "Changing wall order to $wall")
        Log.i("ProfileViewModel", "Current wall order: ${_editedProfile.value?.wall}")
        _editedProfile.value?.wall = wall
    }

    fun changeJob(job: String?) {
        Log.i("ProfileViewModel", "Changing job to $job")
        _editedProfile.value?.job = job
    }

    fun changeHoroscope(horoscope: String?) {
        _editedProfile.value?.horoscope = horoscope
    }

    fun changeEducation(education: String?) {
        Log.i("ProfileViewModel", "Changing education to $education")
        _editedProfile.value?.education = education
    }

    fun changePersonalityType(personalityType: String?) {
        _editedProfile.value?.personalityType = personalityType
    }

    fun changePets(pets: String?) {
        _editedProfile.value?.pets = pets
    }

    fun changeDrinks(drinks: String?) {
        _editedProfile.value?.drinks = drinks
    }

    fun changeSmokes(smokes: String?) {
        _editedProfile.value?.smokes = smokes
    }

    fun changeDoesSports(doesSports: String?) {
        _editedProfile.value?.doesSports = doesSports
    }

    fun changeValuesAndBeliefs(valuesAndBeliefs: String?) {
        _editedProfile.value?.valuesAndBeliefs = valuesAndBeliefs
    }

    fun changeGender(gender: String) {
        _editedProfile.value?.gender = gender
    }

    fun changeSexualOrientation(orientation: String) {
        _editedProfile.value?.sexualOrientation = orientation
    }

    fun changeRelationshipType(relationshipType: String) {
        _editedProfile.value?.relationshipType = relationshipType
    }

    fun loadProfile(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.getProfile(userId)
            result.onSuccess { profileData ->
                _profileData.value = profileData
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error loading profile"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            Log.i("ProfileViewModel", "Updating profile: ${_editedProfile.value}")
            Log.i("ProfileViewModel", "Current profile: ${profileData.value}")

            val updateFunctions = listOf(
                ::updateAboutMe,
                ::updateFact,
                ::updateHeight,
                ::updateWeight,
                ::updateGender,
                ::updateSexualOrientation,
                ::updateJob,
                ::updateHoroscope,
                ::updateEducation,
                ::updatePersonalityType,
                ::updatePets,
                ::updateDrinks,
                ::updateSmokes,
                ::updateDoesSports,
                ::updateValuesAndBeliefs
            )

            updateFunctions.forEach { it() }

            _isLoading.value = false
            loadProfile()
        }
    }



    fun updateAgeRange(min: Int, max: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateAgeRange(userViewModel.userId!!, min, max)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(ageRange = Profile.AgeRange(min, max))
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating age range"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateMaxDistance(maxDistance: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateMaxDistance(userViewModel.userId!!, maxDistance)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(maxDistance = maxDistance)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating max distance"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateGenderPriority(gender: Gender?) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateGenderPriority(userViewModel.userId!!, stringToEnum<Gender>(_editedProfile.value?.genderPriority))
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(genderPriority = _editedProfile.value?.genderPriority.toString())
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating gender priority"
                )
                _isLoading.value = false
            }
        }
    }


    fun updateRelationshipType(relationshipType: RelationshipType) {
        viewModelScope.launch {
            _isLoading.value = true
            val result =
                profileService.updateRelationshipType(userViewModel.userId!!, relationshipType)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(relationshipType = relationshipType.toString())
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating relationship type"
                )
                _isLoading.value = false
            }
        }
    }


    fun updateAboutMe() {
        if (_editedProfile.value?.aboutMe == _profileData.value?.aboutMe) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = _editedProfile.value?.aboutMe?.let {
                profileService.updateAboutMe(userViewModel.userId!!,
                    it
                )
            }
            if (result != null) {
                result.onSuccess {
                    _profileData.value = _editedProfile.value?.aboutMe?.let { it1 -> _profileData.value?.copy(aboutMe = it1) }
                    _isLoading.value = false
                }.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error updating about me"
                    )
                    _isLoading.value = false
                }
            }
        }
    }

    fun updateFact() {
        if (_editedProfile.value?.fact == _profileData.value?.fact) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateFact(userViewModel.userId!!, _editedProfile.value?.fact)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(fact = _editedProfile.value?.fact)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating fact"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateInterests(interests: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateInterests(userViewModel.userId!!, interests)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(interests = interests)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating interests"
                )
                _isLoading.value = false
            }
        }

    }
    fun updateHeight() {
        if (_editedProfile.value?.height == _profileData.value?.height) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateHeight(userViewModel.userId!!, _editedProfile.value?.height)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(height = _editedProfile.value?.height)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating height"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateWeight() {
        if (_editedProfile.value?.weight == _profileData.value?.weight) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateWeight(userViewModel.userId!!, _editedProfile.value?.weight)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(weight = _editedProfile.value?.weight)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating weight"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateGender() {
        if (_editedProfile.value?.genderEnum == _profileData.value?.genderEnum) return
        viewModelScope.launch {
            _isLoading.value = true
            val result =
                _editedProfile.value?.let { profileService.updateGender(userViewModel.userId!!, it.genderEnum) }
            if (result != null) {
                result.onSuccess {
                    _profileData.value = _profileData.value?.copy(gender = _editedProfile.value?.genderEnum.toString())
                    _isLoading.value = false
                }.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error updating gender"
                    )
                    _isLoading.value = false
                }
            }
        }
    }

    fun updateSexualOrientation() {
        if (_editedProfile.value?.sexualOrientationEnum == _profileData.value?.sexualOrientationEnum) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = _editedProfile.value?.sexualOrientationEnum?.let {
                profileService.updateSexualOrientation(userViewModel.userId!!,
                    it
                )
            }
            if (result != null) {
                result.onSuccess {
                    _profileData.value = _profileData.value?.copy(sexualOrientation = _editedProfile.value?.sexualOrientationEnum.toString())
                    _isLoading.value = false
                }.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error updating sexual orientation"
                    )
                    _isLoading.value = false
                }
            }
        }
    }

    fun updateJob() {
        if (_editedProfile.value?.job == _profileData.value?.job) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateJob(userViewModel.userId!!, _editedProfile.value?.job)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(job = _editedProfile.value?.job)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating position"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateHoroscope() {
        if (_editedProfile.value?.horoscopeEnum == _profileData.value?.horoscopeEnum) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateHoroscope(userViewModel.userId!!, _editedProfile.value?.horoscopeEnum)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(horoscope = enumToString(_editedProfile.value?.horoscopeEnum))
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating horoscope"
                )
                _isLoading.value = false
            }

        }
    }

    fun updateEducation() {
        if (_editedProfile.value?.education == _profileData.value?.education) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateEducation(userViewModel.userId!!, _editedProfile.value?.education)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(education = _editedProfile.value?.education)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating education"
                )
                _isLoading.value = false
            }
        }
    }

    fun updatePersonalityType() {
        if (_editedProfile.value?.personalityType == _profileData.value?.personalityType) return
        viewModelScope.launch {
            _isLoading.value = true
            val result =
                profileService.updatePersonalityType(userViewModel.userId!!, _editedProfile.value?.personalityType)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(personalityType = _editedProfile.value?.personalityType)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating personality type"
                )
                _isLoading.value = false
            }
        }
    }

    fun updatePets() {
        if (_editedProfile.value?.pets == _profileData.value?.pets) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updatePets(userViewModel.userId!!, _editedProfile.value?.pets)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(pets = _editedProfile.value?.pets)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating pets"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateDrinks() {
        if (_editedProfile.value?.drinksEnum == _profileData.value?.drinksEnum) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateDrinks(userViewModel.userId!!, _editedProfile.value?.drinksEnum)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(drinks = enumToString(_editedProfile.value?.drinksEnum))
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating drinks"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateSmokes() {
        if (_editedProfile.value?.smokesEnum == _profileData.value?.smokesEnum) return
        if(_editedProfile.value?.smokesEnum == null)
            viewModelScope.launch {
                _isLoading.value = true
                val result = profileService.updateSmokes(userViewModel.userId!!, _editedProfile.value?.smokesEnum)
                result.onSuccess {
                    _profileData.value = _profileData.value?.copy(smokes = enumToString(_editedProfile.value?.smokesEnum))
                    _isLoading.value = false
                }.onFailure { error ->
                    errorViewModel.showError(
                        error.message ?: "Error updating smokes"
                    )
                    _isLoading.value = false
                }
            }
    }

    fun updateDoesSports() {
        if (_editedProfile.value?.doesSportsEnum == _profileData.value?.doesSportsEnum) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateDoesSports(userViewModel.userId!!, _editedProfile.value?.doesSportsEnum)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(doesSports = enumToString(_editedProfile.value?.doesSportsEnum))
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating sports participation"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateValuesAndBeliefs() {
        if (_editedProfile.value?.valuesAndBeliefsEnum == _profileData.value?.valuesAndBeliefsEnum) return
        viewModelScope.launch {
            _isLoading.value = true
            val result =
                profileService.updateValuesAndBeliefs(userViewModel.userId!!, _editedProfile.value?.valuesAndBeliefsEnum)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(valuesAndBeliefs = enumToString(_editedProfile.value?.valuesAndBeliefsEnum))
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating values and beliefs"
                )
                _isLoading.value = false
            }
        }
    }

    fun addImage(imageUrl: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.addImage(userViewModel.userId!!, imageUrl)
            result.onSuccess { imageUrlApi ->
                Log.i("TuMadre", "Adding image: $imageUrlApi")
                _profileData.value = _profileData.value?.copy(
                    wall = _profileData.value?.wall.orEmpty() + imageUrlApi
                )
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error adding image"
                )
                _isLoading.value = false
            }
        }
    }

    fun deleteImage(imageUrl: String) {
        Log.i("TuMadre", "Deleting image: $imageUrl")
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.removeImage(userViewModel.userId!!, imageUrl)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(
                    wall = (_profileData.value?.wall.orEmpty() - imageUrl)
                )
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error adding image"
                )
                _isLoading.value = false
            }
        }
    }
}
