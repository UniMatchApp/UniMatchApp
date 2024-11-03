package com.ulpgc.uniMatch.data.infrastructure.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ulpgc.uniMatch.data.application.services.ProfileService
import com.ulpgc.uniMatch.data.domain.enum.Gender
import com.ulpgc.uniMatch.data.domain.enum.RelationshipType
import com.ulpgc.uniMatch.data.domain.models.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ProfileViewModel(
    private val profileService: ProfileService,
    private val errorViewModel: ErrorViewModel,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _profileData = MutableStateFlow<Profile?>(null)
    val profileData: StateFlow<Profile?> get() = _profileData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.getProfile(authViewModel.userId!!)
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

    fun updateAgeRange(min: Int, max: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateAgeRange(authViewModel.userId!!, min, max)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(ageRange = min to max)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating age range"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateMaxDistance(distance: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateMaxDistance(authViewModel.userId!!, distance)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(maxDistance = distance)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating max distance"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateGenderPriority(gender: Gender) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateGenderPriority(authViewModel.userId!!, gender)

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
            val result = profileService.updateRelationshipType(authViewModel.userId!!, relationshipType)

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



    fun updateAboutMe(aboutMe: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateAboutMe(authViewModel.userId!!, aboutMe)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating about me"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateFact(fact: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateFact(authViewModel.userId!!, fact)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating fact"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateInterests(interest: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateInterests(authViewModel.userId!!, interest)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating interest"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateHeight(height: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateHeight(authViewModel.userId!!, height)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating height"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateWeight(weight: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateWeight(authViewModel.userId!!, weight)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating weight"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateGender(gender: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateGender(authViewModel.userId!!, gender)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating gender"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateSexualOrientation(orientation: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateSexualOrientation(authViewModel.userId!!, orientation)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating sexual orientation"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateJob(position: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateJob(authViewModel.userId!!, position)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating position"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateRelationshipType(relationshipType: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateRelationshipType(authViewModel.userId!!, relationshipType)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating relationship type"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateHoroscope(horoscope: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateHoroscope(authViewModel.userId!!, horoscope)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating horoscope"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateEducation(education: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateEducation(authViewModel.userId!!, education)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating education"
                )
                _isLoading.value = false
            }
        }
    }

    fun updatePersonalityType(personalityType: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updatePersonalityType(authViewModel.userId!!, personalityType)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating personality type"
                )
                _isLoading.value = false
            }
        }
    }

    fun updatePets(pets: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updatePets(authViewModel.userId!!, pets)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating pets"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateDrinks(drinks: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateDrinks(authViewModel.userId!!, drinks)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating drinks"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateSmokes(smokes: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateSmokes(authViewModel.userId!!, smokes)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating smokes"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateDoesSports(doesSports: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateDoesSports(authViewModel.userId!!, doesSports)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating sports participation"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateValuesAndBeliefs(valuesAndBeliefs: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateValuesAndBeliefs(authViewModel.userId!!, valuesAndBeliefs)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating values and beliefs"
                )
                _isLoading.value = false
            }
        }
    }

    fun addInterest(interest: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.addInterest(authViewModel.userId!!, interest)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error adding interest"
                )
                _isLoading.value = false
            }
        }
    }

    fun removeInterest(interest: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.removeInterest(authViewModel.userId!!, interest)
            result.onSuccess {
//                loadProfile()
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error removing interest"
                )
                _isLoading.value = false
            }
        }
    }
}
