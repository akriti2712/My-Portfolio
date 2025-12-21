const recipes = [{
        key: "pasta",
        title: "Creamy Alfredo Pasta",
        image: "https://res.cloudinary.com/dvrblc9r4/image/upload/v1762445472/pasta_w6vd4t.jpg",
        category: "veg",
        ingredients: [
            "200g fettuccine pasta",
            "1 cup heavy cream",
            "2 tbsp butter",
            "1/2 cup grated Parmesan",
            "Salt & pepper to taste",
            "Chopped parsley for garnish"
        ],
        instructions: [
            "Cook pasta as per instructions and drain.",
            "Melt butter in a pan and add cream.",
            "Add Parmesan, stir until smooth.",
            "Add pasta, toss well and season.",
            "Garnish with parsley and serve hot."
        ]
    },
    {
        key: "pizza",
        title: "Cheese Burst Pizza",
        image: "https://res.cloudinary.com/dvrblc9r4/image/upload/v1762445472/pizza_nvpmxl.avif",
        category: "veg",
        ingredients: [
            "1 pizza base",
            "1/2 cup tomato sauce",
            "1 cup mozzarella cheese",
            "Mixed veggies (capsicum, onion, olives)",
            "Oregano & chili flakes"
        ],
        instructions: [
            "Spread sauce on pizza base.",
            "Add veggies and cheese generously.",
            "Bake for 10-12 minutes at 200°C.",
            "Sprinkle oregano and chili flakes.",
            "Slice and serve hot."
        ]
    },
    {
        key: "chicken",
        title: "Grilled Chicken",
        image: "https://res.cloudinary.com/dvrblc9r4/image/upload/v1762445471/chicken_mba2zx.jpg",
        category: "nonveg",
        ingredients: [
            "2 chicken breasts",
            "1 tbsp olive oil",
            "1 tsp garlic powder",
            "Salt & pepper",
            "Lemon wedges"
        ],
        instructions: [
            "Marinate chicken with oil, garlic powder, salt, and pepper.",
            "Grill both sides until golden brown.",
            "Serve hot with lemon wedges."
        ]
    },
    {
        key: "brownie",
        title: "Chocolate Brownie",
        image: "https://res.cloudinary.com/dvrblc9r4/image/upload/v1762445471/browinie_wevuyj.jpg",
        category: "dessert",
        ingredients: [
            "1 cup all-purpose flour",
            "1/2 cup cocoa powder",
            "1 cup sugar",
            "1/2 cup melted butter",
            "2 eggs"
        ],
        instructions: [
            "Mix all ingredients to form a thick batter.",
            "Pour into a greased pan and bake at 180°C for 25 minutes.",
            "Cool and cut into squares."
        ]
    },
    {
        key: "salad",
        title: "Fresh Garden Salad",
        image: "https://res.cloudinary.com/dvrblc9r4/image/upload/v1762445471/salad_isdn1u.jpg",
        category: "quick",
        ingredients: [
            "1 cucumber (chopped)",
            "1 tomato (chopped)",
            "1/2 onion (sliced)",
            "Lettuce leaves",
            "Salt, pepper, and lemon juice"
        ],
        instructions: [
            "Mix all chopped veggies in a bowl.",
            "Add salt, pepper, and lemon juice.",
            "Toss well and serve chilled."
        ]
    },
    {
        key: "burger",
        title: "Veggie Supreme Burger",
        image: "https://images.unsplash.com/photo-1550547660-d9450f859349",
        category: "veg",
        ingredients: [
            "2 burger buns",
            "1 veggie patty",
            "Lettuce & tomato slices",
            "1 slice cheese",
            "Mayonnaise & ketchup"
        ],
        instructions: [
            "Grill or fry the patty until golden.",
            "Toast the buns lightly.",
            "Assemble: bun, lettuce, patty, cheese, tomato, sauces.",
            "Top with the second bun.",
            "Serve with fries."
        ]
    },
];

const gallery = document.getElementById("recipeGallery");
const favouriteGallery = document.getElementById("favouriteGallery");
const recipePopup = document.getElementById("recipe-details");
const favouriteBtn = document.getElementById("favourite-btn");
const themeToggle = document.getElementById("theme-toggle");
let currentRecipe = null;

// Load favourites
let favourites = JSON.parse(localStorage.getItem("favourites")) || [];

function displayRecipes(list, container) {
    container.innerHTML = "";
    list.forEach(r => {
        const card = document.createElement("div");
        card.classList.add("recipe-card");
        card.dataset.recipe = r.key;
        card.innerHTML = `<img src="${r.image}" alt="${r.title}" /><h2>${r.title}</h2>`;
        container.appendChild(card);
    });
}
displayRecipes(recipes, gallery);
updateFavourites();

gallery.addEventListener("click", e => {
    const card = e.target.closest(".recipe-card");
    if (!card) return;
    const recipe = recipes.find(r => r.key === card.dataset.recipe);
    openRecipe(recipe);
});

function openRecipe(recipe) {
    currentRecipe = recipe;
    document.getElementById("recipe-title").textContent = recipe.title;
    document.getElementById("recipe-image").src = recipe.image;
    document.getElementById("recipe-ingredients").innerHTML = recipe.ingredients.map(i => `<li>${i}</li>`).join("");
    document.getElementById("recipe-instructions").innerHTML = recipe.instructions.map(s => `<li>${s}</li>`).join("");
    favouriteBtn.textContent = favourites.some(f => f.key === recipe.key) ?
        "💔 Remove from Favourites" :
        "❤️ Add to Favourites";
    recipePopup.classList.remove("hidden");
}

document.querySelector(".close-btn").addEventListener("click", () => {
    recipePopup.classList.add("hidden");
});

favouriteBtn.addEventListener("click", () => {
    const exists = favourites.find(f => f.key === currentRecipe.key);
    if (exists) {
        favourites = favourites.filter(f => f.key !== currentRecipe.key);
    } else {
        favourites.push(currentRecipe);
    }
    localStorage.setItem("favourites", JSON.stringify(favourites));
    updateFavourites();
    openRecipe(currentRecipe);
});

function updateFavourites() {
    displayRecipes(favourites, favouriteGallery);
}

/* 🔍 SEARCH + FILTER */
document.getElementById("searchInput").addEventListener("input", filterRecipes);
document.getElementById("categoryFilter").addEventListener("change", filterRecipes);

function filterRecipes() {
    const searchValue = document.getElementById("searchInput").value.toLowerCase();
    const category = document.getElementById("categoryFilter").value;
    const filtered = recipes.filter(r =>
        (category === "all" || r.category === category) &&
        r.title.toLowerCase().includes(searchValue)
    );
    displayRecipes(filtered, gallery);
}

/* 🌙 DARK/LIGHT THEME */
themeToggle.addEventListener("click", () => {
    document.body.classList.toggle("dark");
    themeToggle.textContent = document.body.classList.contains("dark") ? "🌞" : "🌙";
});