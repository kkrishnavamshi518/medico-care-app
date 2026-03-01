document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    const nameInput = document.querySelector("input[name='name']");
    const emailInput = document.querySelector("input[name='email']");
    const contactInput = document.querySelector("input[name='contactNo']");
    const passwordInput = document.querySelector("input[name='password']");
    // Create error message element
    function createErrorElement(input) {
        let error = input.parentElement.querySelector(".error-message");
        if (!error) {
            error = document.createElement("div");
            error.className = "error-message text-danger small mt-1";
            input.parentElement.appendChild(error);
        }
        return error;
    }
    // ---------------- NAME VALIDATION ----------------
    nameInput.addEventListener("blur", function () {
        const error = createErrorElement(nameInput);
        if (nameInput.value.trim() === "") {
            error.textContent = "Name is required";
        } else if (nameInput.value.trim().length < 3) {
            error.textContent = "Name must be at least 3 characters";
        } else {
            error.textContent = "";
        }
    });
    // ---------------- EMAIL VALIDATION ----------------
    emailInput.addEventListener("blur", function () {
        const error = createErrorElement(emailInput);
        const emailPattern = /^[^ ]+@[^ ]+\.[a-z]{2,3}$/;
        if (!emailPattern.test(emailInput.value.trim())) {
            error.textContent = "Enter a valid email address";
        } else {
            error.textContent = "";
        }
    });
    // ---------------- CONTACT VALIDATION ----------------
    contactInput.addEventListener("blur", function () {
        const error = createErrorElement(contactInput);
        //const phonePattern = /^[6-9]\d{9}$/; // Indian 10-digit format
        const phonePattern = /^\d{10}$/; //Universal
        if (!phonePattern.test(contactInput.value.trim())) {
            error.textContent = "Enter valid 10-digit mobile number";
        } else {
            error.textContent = "";
        }
    });
    // ---------------- PASSWORD VALIDATION ----------------
    passwordInput.addEventListener("blur", function () {
        const error = createErrorElement(passwordInput);
        const password = passwordInput.value;
        const hasNumber = /[0-9]/.test(password);
        const hasCapital = /[A-Z]/.test(password);
        const hasSpecial = /[!@#$%^&*]/.test(password);
        if (password.length < 6) {
            error.textContent = "Password must be at least 6 characters";
        } else if (!hasNumber) {
            error.textContent = "Password must contain at least one number";
        } else if (!hasCapital) {
            error.textContent = "Password must contain at least one capital letter";
        } else if (!hasSpecial) {
            error.textContent = "Password must contain at least one special character";
        } else {
            error.textContent = "";
        }
    });
    // ---------------- FORM SUBMIT VALIDATION ----------------
    form.addEventListener("submit", function (e) {
        const errors = document.querySelectorAll(".error-message");
        let hasError = false;
        errors.forEach(err => {
            if (err.textContent !== "") {
                hasError = true;
            }
        });
        if (hasError) {
            e.preventDefault(); // Stop form submission
            alert("Please fix validation errors before submitting.");
        }
        // If no errors → form submits normally to backend
    });
});