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
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch



open class ProfileViewModel(
    private val profileService: ProfileService,
    private val errorViewModel: ErrorViewModel,
    private val userViewModel: UserViewModel
) : ViewModel() {

    private val _profileData = MutableStateFlow<Profile?>(null)
    val profileData: StateFlow<Profile?> get() = _profileData

    private var _editedProfile = MutableStateFlow<Profile?>(null)
    val editedProfile: StateFlow<Profile?> get() = _editedProfile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

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

    fun hasUnsavedChanges() : Boolean {
        return _profileData.value != _editedProfile.value
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

    fun changeJob(job: String?) {
        _editedProfile.value?.job = job
    }

    fun changeHoroscope(horoscope: Horoscope?) {
        _editedProfile.value?.horoscope = horoscope
    }

    fun changeEducation(education: String?) {
        _editedProfile.value?.education = education
    }

    fun changePersonalityType(personalityType: String?) {
        _editedProfile.value?.personalityType = personalityType
    }

    fun changePets(pets: String?) {
        _editedProfile.value?.pets = pets
    }

    fun changeDrinks(drinks: Habits?) {
        _editedProfile.value?.drinks = drinks
    }

    fun changeSmokes(smokes: Habits?) {
        _editedProfile.value?.smokes = smokes
    }

    fun changeDoesSports(doesSports: Habits?) {
        _editedProfile.value?.doesSports = doesSports
    }

    fun changeValuesAndBeliefs(valuesAndBeliefs: Religion?) {
        _editedProfile.value?.valuesAndBeliefs = valuesAndBeliefs
    }

    fun changeGender(gender: Gender) {
        _editedProfile.value?.gender = gender
    }

    fun changeSexualOrientation(orientation: SexualOrientation) {
        _editedProfile.value?.sexualOrientation = orientation
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

            listOf(
                launch { updateAboutMe() },
                launch { updateFact() },
                launch { updateHeight() },
                launch { updateWeight() },
                launch { updateJob() },
                launch { updateHoroscope() },
                launch { updateEducation() },
                launch { updatePersonalityType() },
                launch { updatePets() },
                launch { updateDrinks() },
                launch { updateSmokes() },
                launch { updateDoesSports() },
                launch { updateValuesAndBeliefs() },
                launch { updateGender() },
                launch { updateSexualOrientation() }
            ).joinAll()
        }
    }

    fun updateAgeRange(min: Int, max: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateAgeRange(min, max)
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
            val result = profileService.updateMaxDistance(maxDistance)
            result.onSuccess { maxDistance ->
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
            val result = profileService.updateGenderPriority(gender)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(genderPriority = gender)
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
                profileService.updateRelationshipType(relationshipType)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(relationshipType = relationshipType)
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
                profileService.updateAboutMe(it)
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
            val result = profileService.updateFact(_editedProfile.value?.fact)
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
            val result = profileService.updateInterests(interests)
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
            val result = profileService.updateHeight(_editedProfile.value?.height)
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
            val result = profileService.updateWeight(_editedProfile.value?.weight)
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
        if (_editedProfile.value?.gender == _profileData.value?.gender) return
        viewModelScope.launch {
            _isLoading.value = true
            val result =
                _editedProfile.value?.let { profileService.updateGender(it.gender) }
            if (result != null) {
                result.onSuccess {
                    _profileData.value = _editedProfile.value?.gender?.let { it1 -> _profileData.value?.copy(gender = it1) }
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
        if (_editedProfile.value?.sexualOrientation == _profileData.value?.sexualOrientation) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = _editedProfile.value?.sexualOrientation?.let {
                profileService.updateSexualOrientation(
                    it
                )
            }
            if (result != null) {
                result.onSuccess {
                    _profileData.value = _editedProfile.value?.let { it1 -> _profileData.value?.copy(sexualOrientation = it1.sexualOrientation) }
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
            val result = profileService.updateJob(_editedProfile.value?.job)
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
        if (_editedProfile.value?.horoscope == _profileData.value?.horoscope) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateHoroscope(_editedProfile.value?.horoscope)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(horoscope = _editedProfile.value?.horoscope)
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
            val result = profileService.updateEducation(_editedProfile.value?.education)
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
                profileService.updatePersonalityType(_editedProfile.value?.personalityType)
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
            val result = profileService.updatePets(_editedProfile.value?.pets)
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
        if (_editedProfile.value?.drinks == _profileData.value?.drinks) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateDrinks(_editedProfile.value?.drinks)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(drinks = _editedProfile.value?.drinks)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating drinks"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateLocation(location: Profile.Location?) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.i("ProfileViewModel", "Updating location: $location")
            val result = profileService.updateLocation(location)
            result.onSuccess { location2 ->
                _profileData.value = _profileData.value?.copy(location = location)
                _isLoading.value = false
            }.onFailure {
                _isLoading.value = false
            }
        }
    }

    fun updateSmokes() {
        if (_editedProfile.value?.smokes == _profileData.value?.smokes) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateSmokes(_editedProfile.value?.smokes)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(smokes = _editedProfile.value?.smokes)
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
        if (_editedProfile.value?.doesSports == _profileData.value?.doesSports) return
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateDoesSports(_editedProfile.value?.doesSports)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(doesSports = _editedProfile.value?.doesSports)
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
        if (_editedProfile.value?.valuesAndBeliefs == _profileData.value?.valuesAndBeliefs) return
        viewModelScope.launch {
            _isLoading.value = true
            val result =
                profileService.updateValuesAndBeliefs(_editedProfile.value?.valuesAndBeliefs)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(valuesAndBeliefs = _editedProfile.value?.valuesAndBeliefs)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating values and beliefs"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateWall(wall: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateWall(wall)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(wall = wall)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating wall"
                )
                _isLoading.value = false
            }
        }
    }

    fun addImage(imageUrl: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.addImage(imageUrl)
            result.onSuccess { imageUrlApi ->
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
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.removeImage(imageUrl)
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
