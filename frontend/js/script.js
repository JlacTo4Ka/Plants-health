'use strict'

document.addEventListener('DOMContentLoaded', () => {
	const firstPage = document.querySelector('.firstPage')
	const secondPage = document.querySelector('.secondPage')
	const uploadPhotoDiv = document.querySelector('.uploadPhoto')
	const nameDiseaseDiv = document.querySelector('.nameDisease')
	const descriptionDiseaseDiv = document.querySelector('.descriptionDisease')

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

	const listDieses = document.querySelector('.listDieses')
	const btnUploadPhoto = document.querySelector('.btnUploadPhoto')

	// Универсальная функция для JSON-запросов (регистрация/логин)
	async function sendRequestJSON(url, method, body, token) {
		const headers = { 'Content-Type': 'application/json' }
		if (token) {
			headers['Authorization'] = `Bearer ${token}`
		}

		const response = await fetch(url, {
			method: method,
			headers,
			body: body ? JSON.stringify(body) : null,
		})

		if (!response.ok) {
			const errorMessage = await response.text()
			throw new Error(errorMessage)
		}

		return await response.json()
	}

	// Получение истории болезней (GET /api/v1/plants/history)
	async function fetchDiseasesHistory() {
		const token = localStorage.getItem('token')
		if (!token) {
			// Если нужно, чтобы неавторизованным не показывалось вовсе,
			// можно вернуть alert или пустую историю.
			alert('Нет токена — история недоступна.')
			return
		}

		try {
			const response = await fetch(
				`http://62.233.43.155:8080/api/v1/plants/history`,
				{
					method: 'GET',
					headers: {
						Authorization: token,
					},
				}
			)

			if (!response.ok) {
				const errText = await response.text()
				throw new Error(errText)
			}

			const data = await response.json()
			if (data && data.items) {
				renderDiseases(data.items)
			} else {
				listDieses.innerHTML = 'Нет данных в истории.'
			}
		} catch (error) {
			console.error(error)
			alert(`Ошибка при получении истории: ${error.message}`)
		}
	}

	// Отрисовать историю болезней
	function renderDiseases(diseases) {
		listDieses.innerHTML = '' // очистка

		diseases.forEach(item => {
			const card = document.createElement('div')
			card.classList.add('disease-card')

			// Картинка болезни (если есть imageUrl)
			if (item.imageUrl) {
				const img = document.createElement('img')
				img.src = item.imageUrl
				card.appendChild(img)
			}

			// Название болезни
			const title = document.createElement('h4')
			title.textContent = item.disease ?? 'Неизвестная болезнь'
			card.appendChild(title)

			listDieses.appendChild(card)
		})
	}

	// ====== Регистрация (POST /api/v1/auth/signup)
	btnSignUp.addEventListener('click', async event => {
		event.preventDefault()

		const login = loginInputSignUp.value.trim()
		const password = passwordInputSignUp.value.trim()

		if (!login || !password) {
			alert('Введите логин и пароль')
			return
		}

		try {
			const data = await sendRequestJSON(
				'http://62.233.43.155:8080/api/v1/auth/signup',
				'POST',
				{ login, password }
			)
			localStorage.setItem('token', data.token)
			alert('Регистрация успешна!')
			// Закрываем форму, возвращаемся на firstPage
			signPage.style.display = 'none'
			firstPage.style.display = 'flex'
			profile.style.display = 'block'
			close.style.display = 'none'
		} catch (error) {
			alert(`Ошибка регистрации: ${error.message}`)
		}
	})

	// ====== Авторизация (POST /api/v1/auth/signin)
	btnSignIn.addEventListener('click', async event => {
		event.preventDefault()

		const login = loginInputSignIn.value.trim()
		const password = passwordInputSignIn.value.trim()

		if (!login || !password) {
			alert('Введите логин и пароль')
			return
		}

		try {
			const data = await sendRequestJSON(
				'http://62.233.43.155:8080/api/v1/auth/signin',
				'POST',
				{ login, password }
			)
			localStorage.setItem('token', data.token)
			alert('Вход выполнен успешно!')
			// Закрываем форму, возвращаемся на firstPage
			signPage.style.display = 'none'
			firstPage.style.display = 'flex'
			profile.style.display = 'block'
			close.style.display = 'none'
		} catch (error) {
			alert(`Ошибка авторизации: ${error.message}`)
		}
	})

	// Нажатие на кнопку account
	profile.addEventListener('click', async event => {
		event.preventDefault()

		// Проверяем, есть ли токен
		const token = localStorage.getItem('token')
		if (!token) {
			// Нет токена -> показываем форму входа/регистрации
			firstPage.style.display = 'none'
			secondPage.style.display = 'none'
			profile.style.display = 'none'
			close.style.display = 'block'
			signPage.style.display = 'flex'
		} else {
			// Есть токен -> показываем историю
			firstPage.style.display = 'none'
			secondPage.style.display = 'none'
			signPage.style.display = 'none'
			close.style.display = 'block'
			profile.style.display = 'none'
			listDieses.style.display = 'flex' // показать блок
			// Загружаем и рендерим
			await fetchDiseasesHistory()
		}
	})

	// Кнопка закрытия (крестик)
	close.addEventListener('click', event => {
		event.preventDefault()
		// Возвращаемся на первую страницу
		firstPage.style.display = 'flex'
		secondPage.style.display = 'none'
		profile.style.display = 'block'
		close.style.display = 'none'
		signPage.style.display = 'none'
		listDieses.style.display = 'none'
	})

	// Переключение между табами "Sign In" и "Sign Up"
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

	// Загрузка фото => POST /api/v1/plants/disease
	btnUploadPhoto.addEventListener('click', async event => {
		event.preventDefault()

		// Пытаемся взять токен (может быть null)
		const token = localStorage.getItem('token')

		// Создаём <input type="file" accept="image/*">
		const fileInput = document.createElement('input')
		fileInput.type = 'file'
		fileInput.accept = 'image/*'

		fileInput.addEventListener('change', async () => {
			const file = fileInput.files[0]
			if (file) {
				// Отображаем локально загруженную картинку
				const reader = new FileReader()
				reader.onload = e => {
					uploadPhotoDiv.style.backgroundImage = `url('${e.target.result}')`
				}
				reader.readAsDataURL(file)

				try {
					// multipart/form-data
					const formData = new FormData()
					formData.append('img', file) // key: "img" (см. Swagger)

					// Готовим fetch
					const fetchOptions = {
						method: 'POST',
						body: formData,
					}

					// Если есть токен – добавим заголовок Authorization
					if (token) {
						fetchOptions.headers = {
							Authorization: token,
						}
					}

					const response = await fetch(
						'http://62.233.43.155:8080/api/v1/plants/disease',
						fetchOptions
					)

					if (!response.ok) {
						const errText = await response.text()
						throw new Error(errText)
					}

					// Ответ: { "disease": "...", "description": "..." }
					const data = await response.json()

					firstPage.style.display = 'none'
					secondPage.style.display = 'flex'

					nameDiseaseDiv.textContent = data.disease ?? 'Неизвестная болезнь'
					if (data.description) {
						const htmlDescription = marked.parse(data.description)
						descriptionDiseaseDiv.innerHTML = htmlDescription
					} else {
						descriptionDiseaseDiv.textContent = 'Описание отсутствует'
					}
				} catch (err) {
					console.error(err)
					alert(`Ошибка при распознавании болезни: ${err.message}`)
				}
			}
		})

		fileInput.click()
	})
})
