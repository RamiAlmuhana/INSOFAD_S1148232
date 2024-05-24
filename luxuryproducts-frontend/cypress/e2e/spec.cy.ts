describe('Promo Code Test in Cart Page with Mock Data', () => {
  beforeEach(() => {
    // Intercept the API call to get products and mock the response
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [
        {
          "id": 1,
          "name": "Tom Clancy's Rainbow Six Siege",
          "description": "Tom Clancy's Rainbow Six® Siege is an elite, tactical team-based shooter where superior planning and execution triumph.",
          "price": 15.99,
          "imgURL": "https://store.ubisoft.com/on/demandware.static/-/Sites-masterCatalog/default/dw63e24d90/images/large/56c494ad88a7e300458b4d5a.jpg",
          "specifications": "OS *: Originally released for Windows 7, the game can be played on Windows 10 and Windows 11 OSProcessor: Intel Core i5-2500K @ 3.3 GHz or better or AMD FX-8120 @ 3.1 Ghz or better\nMemory: 8 GB RAM\nGraphics: NVIDIA GeForce GTX 670 or AMD Radeon HD 7970",
          "publisher": "Ubisoft",
          "releaseDate": "1-Dec-2015",
          "categoryId": 1,
          "categoryName": "FPS"
        }
      ]
    }).as('getProducts');

    // Intercept the API call to apply promo code and mock the response
    cy.intercept('POST', '/api/promocodes/validate', (req) => {
      req.reply((res) => {
        if (req.body.code === 'PROMOCODE123') {
          res.send({
            statusCode: 200,
            body: {
              discount: 10,
              type: 'PERCENTAGE',
              minSpendAmount: 50,
              startDate: '2024-01-01T00:00:00.000Z',
              expiryDate: '2024-12-31T23:59:59.000Z'
            }
          });
        } else {
          res.send({
            statusCode: 400,
            body: {
              error: 'Invalid promo code'
            }
          });
        }
      });
    }).as('applyPromoCode');
  });

  it('should add a product to the cart and apply a promo code', () => {
    // Visit the home page
    cy.visit('http://localhost:4200/products');

    // Wait for the products API call and verify it
    cy.wait('@getProducts').its('response.statusCode').should('eq', 200);

    // Add a product to the cart
    cy.contains('.test', 'Buy').parent().find('button').click()

    // Go to the cart page
    cy.visit('http://localhost:4200/cart');

    // Apply the promo code
    cy.get('input[name="promoCode"]').type('SUMMER2024'); // Verander 'PROMO2024' naar de daadwerkelijke promotiecode
    cy.get('button').contains('Apply').click();
    // cy.get('[data-cy=promo-code-input]').type('PROMOCODE123');
    // cy.get('[data-cy=apply-promo-code-button]').click();

    // Wait for the promo code API call and verify it
    cy.wait('@applyPromoCode').its('response.statusCode').should('eq', 200);

    // Verify the discount is applied
    cy.get('[data-cy=total-price]').should('contain', '9'); // Assuming the total price is 9 after applying a 10% discount
  });
});
