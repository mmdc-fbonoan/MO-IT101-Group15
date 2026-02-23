# Guide: Writing Tests (Happy Path, Invalid Input, Edge Cases)

Use this simple structure for most unit tests:

1. **Success / Happy path** — valid input behaves correctly
2. **Invalid input** — wrong input is handled safely (usually throws)
3. **Edge / Boundary cases** — “weird but valid” inputs (empty, 0, min/max)

---

## 1) Decide the Rule of the Method First

Before writing tests, answer:

- What inputs are **valid**?
- What inputs are **invalid**?
- For invalid inputs, should the method **throw** or **return a default**?

Write that down briefly in a comment or docstring.

---

## 2) Make a Test Checklist

For each method, list test scenarios:

### ✅ Success (Happy Path)
- Typical valid values
- Expected output

### ❌ Invalid
- `null` (if reference types)
- negative values (if not allowed)
- wrong ranges (e.g., age < 0)
- malformed strings (if parsing)

### ⚠️ Edge / Boundary
- empty string `""`
- whitespace `"   "`
- zero `0`
- min/max values (e.g., `Integer.MIN_VALUE`, `Integer.MAX_VALUE`)
- very large input size (if relevant)

---

## 3) Use Arrange–Act–Assert (AAA)

Structure each test in 3 sections:

- **Arrange**: setup data and objects
- **Act**: call the method
- **Assert**: verify result

Keep each test focused on **one behavior**.
