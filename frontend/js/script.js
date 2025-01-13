'use strict'

document.addEventListener('DOMContentLoaded', () => {
	const btnUploadPhoto = document.querySelector('.btnUploadPhoto')
	const firstPage = document.querySelector('.firstPage')
	const secondPage = document.querySelector('.secondPage')
	const uploadPhoto = document.querySelector('.uploadPhoto')
	const profile = document.querySelector('.account')
	const close = document.querySelector('.close')
	const signPage = document.querySelector('.signPage')
	const signIn = document.querySelector('.signIn')
	const signUp = document.querySelector('.signUp')
	const formSignIn = document.querySelector('.formSignIn')
	const formSignUp = document.querySelector('.formSignUp')

	profile.addEventListener('click', event => {
		event.preventDefault()

		firstPage.style.display = 'none'
		secondPage.style.display = 'none'
		profile.style.display = 'none'
		close.style.display = 'block'
		signPage.style.display = 'flex'
	})

	close.addEventListener('click', event => {
		event.preventDefault()

		firstPage.style.display = 'flex'
		profile.style.display = 'block'
		close.style.display = 'none'
		signPage.style.display = 'none'
	})

	signUp.addEventListener('click', event => {
		event.preventDefault()

		signIn.style.background = 'rgba(255, 255, 255, 0)'
		signUp.style.background = 'rgba(255, 255, 255, 1)'
		formSignIn.style.display = 'none'
		formSignUp.style.display = 'block'
	})

	signIn.addEventListener('click', event => {
		event.preventDefault()

		signIn.style.background = 'rgba(255, 255, 255, 1)'
		signUp.style.background = 'rgba(255, 255, 255, 0)'
		formSignIn.style.display = 'block'
		formSignUp.style.display = 'none'
	})

	btnUploadPhoto.addEventListener('click', event => {
		event.preventDefault()

		const fileInput = document.createElement('input')
		fileInput.type = 'file'
		fileInput.accept = 'image/*'

		fileInput.addEventListener('change', () => {
			const file = fileInput.files[0]
			if (file) {
				const reader = new FileReader()

				reader.onload = e => {
					uploadPhoto.style.backgroundImage = `url('${e.target.result}')`
					uploadPhoto.style.backgroundSize = 'cover'
					uploadPhoto.style.backgroundPosition = 'center'

					firstPage.style.display = 'none'
					secondPage.style.display = 'flex'
				}

				reader.readAsDataURL(file)
			}
		})

		fileInput.click()
	})
})
