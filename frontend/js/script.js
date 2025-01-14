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
	const btnSignIn = document.querySelector('.formSignIn .btn')
	const btnSignUp = document.querySelector('.formSignUp .btn')
	const loginInputSignIn = document.querySelector('.formSignIn #login')
	const passwordInputSignIn = document.querySelector('.formSignIn #password')
	const loginInputSignUp = document.querySelector('.formSignUp #login')
	const passwordInputSignUp = document.querySelector('.formSignUp #password')

	async function sendRequest(url, method, body) {
		const response = await fetch(url, {
			method: method,
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(body),
		})

		if (!response.ok) {
			const errorMessage = await response.text()
			throw new Error(errorMessage)
		}

		return await response.json()
	}

	// Регистрация
	btnSignUp.addEventListener('click', async event => {
		event.preventDefault()

		const login = loginInputSignUp.value.trim()
		const password = passwordInputSignUp.value.trim()

		if (!login || !password) {
			alert('Введите логин и пароль')
			return
		}

		try {
			const data = await sendRequest(
				'http://62.233.43.155:8080/api/v1/auth/signup',
				'POST',
				{ login, password }
			)
			localStorage.setItem('token', data.token)
			alert('Регистрация успешна!')
			// Перенаправление или другие действия
		} catch (error) {
			alert(`Ошибка регистрации: ${error.message}`)
		}
	})

	// Авторизация
	btnSignIn.addEventListener('click', async event => {
		event.preventDefault()

		const login = loginInputSignIn.value.trim()
		const password = passwordInputSignIn.value.trim()

		if (!login || !password) {
			alert('Введите логин и пароль')
			return
		}

		try {
			const data = await sendRequest(
				'http://62.233.43.155:8080/api/v1/auth/signin',
				'POST',
				{
					login,
					password,
				}
			)
			localStorage.setItem('token', data.token)
			alert('Вход выполнен успешно!')
			// Перенаправление или другие действия
		} catch (error) {
			alert(`Ошибка авторизации: ${error.message}`)
		}
	})

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
