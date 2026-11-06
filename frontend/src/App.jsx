
import { useEffect, useState } from 'react'
import axios from 'axios'
import './App.css'

const emptyForm = {
  cim: '',
  ev: '',
  hossz: ''
}

function App() {
  const [filmek, setFilmek] = useState([])
  const [form, setForm] = useState(emptyForm)
  const [editingId, setEditingId] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [message, setMessage] = useState('')
  const [search, setSearch] = useState('')

  const loadFilmek = async () => {
    try {
      const response = await axios.get('/api/filmek')
      setFilmek(response.data)
      setError('')
    } catch {
      setError('Nem sikerült betölteni a filmeket.')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadFilmek()
  }, [])

  const handleChange = (event) => {
    const { name, value } = event.target
    setForm((previous) => ({
      ...previous,
      [name]: value
    }))
  }

  const resetForm = () => {
    setForm(emptyForm)
    setEditingId(null)
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setMessage('')

    const payload = {
      cim: form.cim.trim(),
      ev: Number(form.ev),
      hossz: Number(form.hossz)
    }

    if (!payload.cim || !Number.isInteger(payload.ev) ||
        !Number.isInteger(payload.hossz) ||
        payload.ev < 1888 || payload.ev > 2100 ||
        payload.hossz <= 0) {
      setError('Adj meg érvényes filmcímet, évszámot és játékidőt!')
      return
    }

    try {
      if (editingId !== null) {
        await axios.put(`/api/filmek/${editingId}`, payload)
        setMessage('A film módosítása sikeres.')
      } else {
        await axios.post('/api/filmek', payload)
        setMessage('Az új film sikeresen hozzáadva.')
      }

      resetForm()
      await loadFilmek()
    } catch {
      setError('A film mentése sikertelen.')
    }
  }

  const handleEdit = (film) => {
    setEditingId(film.id)
    setForm({
      cim: film.cim,
      ev: String(film.ev),
      hossz: String(film.hossz)
    })
    setMessage('')
    setError('')
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const handleDelete = async (film) => {
    if (!window.confirm(`Biztosan törlöd ezt a filmet: ${film.cim}?`)) {
      return
    }

    try {
      await axios.delete(`/api/filmek/${film.id}`)
      setMessage('A film törlése sikeres.')
      setError('')

      if (editingId === film.id) {
        resetForm()
      }

      await loadFilmek()
    } catch {
      setError('A film törlése sikertelen.')
    }
  }

  const filteredFilmek = filmek.filter((film) =>
      film.cim.toLowerCase().includes(search.toLowerCase())
  )

  return (
      <div className="app-container">
        <header className="app-header">
          <h1>Filmkezelő</h1>
          <p>React + Axios + Spring Boot + H2</p>
        </header>

        <main>
          <section className="card">
            <h2>{editingId !== null ? 'Film módosítása' : 'Új film hozzáadása'}</h2>

            <form onSubmit={handleSubmit} className="film-form">
              <div className="form-group">
                <label htmlFor="cim">Film címe</label>
                <input
                    id="cim"
                    name="cim"
                    type="text"
                    value={form.cim}
                    onChange={handleChange}
                    required
                />
              </div>

              <div className="form-group">
                <label htmlFor="ev">Megjelenés éve</label>
                <input
                    id="ev"
                    name="ev"
                    type="number"
                    min="1888"
                    max="2100"
                    value={form.ev}
                    onChange={handleChange}
                    required
                />
              </div>

              <div className="form-group">
                <label htmlFor="hossz">Hossz (perc)</label>
                <input
                    id="hossz"
                    name="hossz"
                    type="number"
                    min="1"
                    value={form.hossz}
                    onChange={handleChange}
                    required
                />
              </div>

              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  {editingId !== null ? 'Módosítás mentése' : 'Film hozzáadása'}
                </button>

                {editingId !== null && (
                    <button
                        type="button"
                        className="btn btn-secondary"
                        onClick={resetForm}
                    >
                      Mégse
                    </button>
                )}
              </div>
            </form>

            {error && <p className="alert alert-error">{error}</p>}
            {message && <p className="alert alert-success">{message}</p>}
          </section>

          <section className="card">
            <div className="list-header">
              <div>
                <h2>Filmek listája</h2>
                <p>Összes film: {filmek.length}</p>
              </div>

              <input
                  type="search"
                  placeholder="Keresés filmcím alapján..."
                  value={search}
                  onChange={(event) => setSearch(event.target.value)}
              />
            </div>

            {loading ? (
                <p>Filmek betöltése...</p>
            ) : (
                <div className="table-wrapper">
                  <table>
                    <thead>
                    <tr>
                      <th>ID</th>
                      <th>Cím</th>
                      <th>Év</th>
                      <th>Hossz</th>
                      <th>Műveletek</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredFilmek.map((film) => (
                        <tr key={film.id}>
                          <td>{film.id}</td>
                          <td>{film.cim}</td>
                          <td>{film.ev}</td>
                          <td>{film.hossz} perc</td>
                          <td>
                            <div className="table-actions">
                              <button
                                  className="btn btn-edit"
                                  onClick={() => handleEdit(film)}
                              >
                                Szerkesztés
                              </button>
                              <button
                                  className="btn btn-delete"
                                  onClick={() => handleDelete(film)}
                              >
                                Törlés
                              </button>
                            </div>
                          </td>
                        </tr>
                    ))}
                    </tbody>
                  </table>

                  {filteredFilmek.length === 0 && (
                      <p className="empty-message">Nem található film.</p>
                  )}
                </div>
            )}
          </section>
        </main>
      </div>
  )
}

export default App
