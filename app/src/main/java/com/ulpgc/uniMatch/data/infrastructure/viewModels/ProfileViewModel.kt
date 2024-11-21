package com.ulpgc.uniMatch.data.infrastructure.viewModels

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
                _editedProfile.value = profileData.copy() // Copia profunda
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

    fun changeHeight(height: Int) {
        _editedProfile.value?.height = height
    }

    fun changeWeight(weight: Int) {
        _editedProfile.value?.weight = weight
    }

    fun changeJob(job: String) {
        _editedProfile.value?.job = job
    }

    fun changeHoroscope(horoscope: String) {
        _editedProfile.value?.horoscope = horoscope
    }

    fun changeEducation(education: String) {
        _editedProfile.value?.education = education
    }

    fun changePersonalityType(personalityType: String) {
        _editedProfile.value?.personalityType = personalityType
    }

    fun changePets(pets: String) {
        _editedProfile.value?.pets = pets
    }

    fun changeDrinks(drinks: String) {
        _editedProfile.value?.drinks = drinks
    }

    fun changeSmokes(smokes: String) {
        _editedProfile.value?.smokes = smokes
    }

    fun changeDoesSports(doesSports: String) {
        _editedProfile.value?.doesSports = doesSports
    }

    fun changeValuesAndBeliefs(valuesAndBeliefs: String) {
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
            val updateMap = mapOf(
                _editedProfile.value?.aboutMe to { _editedProfile.value?.aboutMe?.let {
                    updateAboutMe(
                        it
                    )
                } },
                _editedProfile.value?.fact to { _editedProfile.value?.fact?.let { updateFact(it) } },
                _editedProfile.value?.height to { _editedProfile.value?.height?.let { updateHeight(it) } },
                _editedProfile.value?.weight to { _editedProfile.value?.weight?.let { updateWeight(it) } },
                _editedProfile.value?.genderEnum to { _editedProfile.value?.genderEnum?.let {
                    updateGender(
                        it
                    )
                } },
                _editedProfile.value?.sexualOrientationEnum to { _editedProfile.value?.sexualOrientationEnum?.let {
                    updateSexualOrientation(
                        it
                    )
                } },
                _editedProfile.value?.job to { _editedProfile.value?.job?.let { updateJob(it) } },
                _editedProfile.value?.relationshipTypeEnum to { _editedProfile.value?.relationshipTypeEnum?.let {
                    updateRelationshipType(
                        it
                    )
                } },
                _editedProfile.value?.horoscopeEnum to { _editedProfile.value?.horoscopeEnum?.let { updateHoroscope(it) } },
                _editedProfile.value?.education to { _editedProfile.value?.education?.let { updateEducation(it) } },
                _editedProfile.value?.personalityType to { _editedProfile.value?.personalityType?.let { updatePersonalityType(it) } },
                _editedProfile.value?.pets to { _editedProfile.value?.pets?.let { updatePets(it) } },
                _editedProfile.value?.drinksEnum to { _editedProfile.value?.drinksEnum?.let { updateDrinks(it) } },
                _editedProfile.value?.smokesEnum to { _editedProfile.value?.smokesEnum?.let { updateSmokes(it) } },
                _editedProfile.value?.doesSportsEnum to { _editedProfile.value?.doesSportsEnum?.let { updateDoesSports(it) } },
                _editedProfile.value?.valuesAndBeliefsEnum to { _editedProfile.value?.valuesAndBeliefsEnum?.let { updateValuesAndBeliefs(it) } },
                _editedProfile.value?.interests to { _editedProfile.value?.interests?.let { updateInterests(it) } }
            )

            updateMap.forEach { (newValue, updateFunction) ->
                val currentValue = when (newValue) {
                    _editedProfile.value?.aboutMe -> profileData.value?.aboutMe
                    _editedProfile.value?.fact -> profileData.value?.fact
                    _editedProfile.value?.interests -> profileData.value?.interests
                    _editedProfile.value?.height -> profileData.value?.height
                    _editedProfile.value?.weight -> profileData.value?.weight
                    _editedProfile.value?.genderEnum -> profileData.value?.genderEnum
                    _editedProfile.value?.sexualOrientationEnum -> profileData.value?.sexualOrientationEnum
                    _editedProfile.value?.job -> profileData.value?.job
                    _editedProfile.value?.relationshipTypeEnum -> profileData.value?.relationshipTypeEnum
                    _editedProfile.value?.horoscopeEnum -> profileData.value?.horoscopeEnum
                    _editedProfile.value?.education -> profileData.value?.education
                    _editedProfile.value?.personalityType -> profileData.value?.personalityType
                    _editedProfile.value?.pets -> profileData.value?.pets
                    _editedProfile.value?.drinksEnum -> profileData.value?.drinksEnum
                    _editedProfile.value?.smokesEnum -> profileData.value?.smokesEnum
                    _editedProfile.value?.doesSportsEnum -> profileData.value?.doesSportsEnum
                    _editedProfile.value?.valuesAndBeliefsEnum -> profileData.value?.valuesAndBeliefsEnum
                    else -> null
                }

                if (newValue != currentValue) {
                    Log.i("TuMadre", "Updating")
                    updateFunction()
                }
            }

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

    fun updateMaxDistance(distance: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateMaxDistance(userViewModel.userId!!, distance)
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

    fun updateGenderPriority(gender: Gender?) {
        viewModelScope.launch {
            _isLoading.value = true

            val result = profileService.updateGenderPriority(userViewModel.userId!!, gender)

            result.onSuccess {
                _profileData.value = _profileData.value?.copy(genderPriority = gender.toString())
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


    fun updateAboutMe(aboutMe: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateAboutMe(userViewModel.userId!!, aboutMe)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(aboutMe = aboutMe)
                _isLoading.value = false
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
            val result = profileService.updateFact(userViewModel.userId!!, fact)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(fact = fact)
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
    fun updateHeight(height: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateHeight(userViewModel.userId!!, height)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(height = height)
                _isLoading.value = false
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
            val result = profileService.updateWeight(userViewModel.userId!!, weight)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(weight = weight)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating weight"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateGender(gender: Gender) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateGender(userViewModel.userId!!, gender)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(gender = gender.toString())
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating gender"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateSexualOrientation(orientation: SexualOrientation) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateSexualOrientation(userViewModel.userId!!, orientation)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(sexualOrientation = orientation.toString())
                _isLoading.value = false
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
            val result = profileService.updateJob(userViewModel.userId!!, position)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(job = position)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating position"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateHoroscope(horoscope: Horoscope) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateHoroscope(userViewModel.userId!!, horoscope)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(horoscope = horoscope.toString())
                _isLoading.value = false
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
            val result = profileService.updateEducation(userViewModel.userId!!, education)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(education = education)
                _isLoading.value = false
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
            val result =
                profileService.updatePersonalityType(userViewModel.userId!!, personalityType)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(personalityType = personalityType)
                _isLoading.value = false
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
            val result = profileService.updatePets(userViewModel.userId!!, pets)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(pets = pets)
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating pets"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateDrinks(drinks: Habits) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateDrinks(userViewModel.userId!!, drinks)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(drinks = drinks.toString())
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating drinks"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateSmokes(smokes: Habits) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateSmokes(userViewModel.userId!!, smokes)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(smokes = smokes.toString())
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating smokes"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateDoesSports(doesSports: Habits) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.updateDoesSports(userViewModel.userId!!, doesSports)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(doesSports = doesSports.toString())
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating sports participation"
                )
                _isLoading.value = false
            }
        }
    }

    fun updateValuesAndBeliefs(valuesAndBeliefs: Religion) {
        viewModelScope.launch {
            _isLoading.value = true
            val result =
                profileService.updateValuesAndBeliefs(userViewModel.userId!!, valuesAndBeliefs)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(valuesAndBeliefs = valuesAndBeliefs.toString())
                _isLoading.value = false
            }.onFailure { error ->
                errorViewModel.showError(
                    error.message ?: "Error updating values and beliefs"
                )
                _isLoading.value = false
            }
        }
    }

    fun addImage(imageUrl: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.addImage(userViewModel.userId!!, imageUrl)
            result.onSuccess {
                _profileData.value =
                    _profileData.value?.copy(wall = _profileData.value?.wall.orEmpty() + imageUrl)
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
            val result = profileService.removeImage(userViewModel.userId!!, imageUrl)
            result.onSuccess {
                _profileData.value = _profileData.value?.copy(
                    wall = (_profileData.value?.wall ?: emptyList()).filter { it != imageUrl }
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
