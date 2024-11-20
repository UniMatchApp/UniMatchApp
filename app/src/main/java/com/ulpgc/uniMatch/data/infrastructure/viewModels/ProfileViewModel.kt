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

    fun getProfileData(): Profile? {
        return _profileData.value
    }

    fun loadProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = profileService.getProfile(userViewModel.userId!!)
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

    fun updateProfile(profile: Profile) {
        viewModelScope.launch {
            _isLoading.value = true
            Log.i("ProfileViewModel", "Updating profile: $profile")
            Log.i("ProfileViewModel", "Current profile: ${profileData.value}")
            val updateMap = mapOf(
                profile.aboutMe to { updateAboutMe(profile.aboutMe) },
                profile.fact to { profile.fact?.let { updateFact(it) } },
                profile.height to { profile.height?.let { updateHeight(it) } },
                profile.weight to { profile.weight?.let { updateWeight(it) } },
                profile.genderEnum to { updateGender(profile.genderEnum) },
                profile.sexualOrientationEnum to { updateSexualOrientation(profile.sexualOrientationEnum) },
                profile.job to { profile.job?.let { updateJob(it) } },
                profile.relationshipTypeEnum to { updateRelationshipType(profile.relationshipTypeEnum) },
                profile.horoscopeEnum to { profile.horoscopeEnum?.let { updateHoroscope(it) } },
                profile.education to { profile.education?.let { updateEducation(it) } },
                profile.personalityType to {
                    profile.personalityType?.let {
                        updatePersonalityType(
                            it
                        )
                    }
                },
                profile.pets to { profile.pets?.let { updatePets(it) } },
                profile.drinksEnum to { profile.drinksEnum?.let { updateDrinks(it) } },
                profile.smokesEnum to { profile.smokesEnum?.let { updateSmokes(it) } },
                profile.doesSportsEnum to { profile.doesSportsEnum?.let { updateDoesSports(it) } },
                profile.valuesAndBeliefsEnum to {
                    profile.valuesAndBeliefsEnum?.let {
                        updateValuesAndBeliefs(
                            it
                        )
                    }
                }
            )

            updateMap.forEach { (newValue, updateFunction) ->
                val currentValue = when (newValue) {
                    profile.aboutMe -> profileData.value?.aboutMe
                    profile.fact -> profileData.value?.fact
                    profile.interests -> profileData.value?.interests
                    profile.height -> profileData.value?.height
                    profile.weight -> profileData.value?.weight
                    profile.genderEnum -> profileData.value?.genderEnum
                    profile.sexualOrientationEnum -> profileData.value?.sexualOrientationEnum
                    profile.job -> profileData.value?.job
                    profile.relationshipTypeEnum -> profileData.value?.relationshipTypeEnum
                    profile.horoscopeEnum -> profileData.value?.horoscopeEnum
                    profile.education -> profileData.value?.education
                    profile.personalityType -> profileData.value?.personalityType
                    profile.pets -> profileData.value?.pets
                    profile.drinksEnum -> profileData.value?.drinksEnum
                    profile.smokesEnum -> profileData.value?.smokesEnum
                    profile.doesSportsEnum -> profileData.value?.doesSportsEnum
                    profile.valuesAndBeliefsEnum -> profileData.value?.valuesAndBeliefsEnum
                    else -> null
                }
                if (newValue != currentValue) {
                    Log.i("TuMadre", "Updating")
                    updateFunction()
                }
            }

        }
        _isLoading.value = false
        loadProfile()
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
                    wall = (_profileData.value?.wallList.orEmpty() - imageUrl).joinToString(", ")
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
